package ru.project.plusone.service.Impl;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.plusone.config.property.VkConfig;
import ru.project.plusone.exception.UserException;
import ru.project.plusone.model.Event;
import ru.project.plusone.model.SocialNetwork;
import ru.project.plusone.model.User;
import ru.project.plusone.service.interfaces.VKService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;

@Service
public class VKServiceImpl implements VKService {
    private UserActor botVk;
    private VkApiClient vkApi;
    private String vkBotAccessToken;
    private int vkBotId;

    @Autowired
    public VKServiceImpl(VkConfig vkConfig) {
        vkBotId = vkConfig.getVkBotId();
        vkBotAccessToken = vkConfig.getVkBotAccessToken();
    }


    @Override
    public Integer sendMessage(User user, String msg) throws ClientException {
        try {
            return vkApi.messages().send(botVk).message(msg).userId(getVkID(user)).execute();
        } catch (ApiException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String joinToConf(Event event) {
        try {
            return new JSONObject(vkApi.messages().getInviteLink(botVk).peerId(event.getChatId() + 2000000000).executeAsString()).getJSONObject("response").getString("link");
        } catch (ClientException | JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean isUserFriend(User user) {
        try {
            return new JSONObject(vkApi.friends().areFriends(botVk, getVkID(user)).executeAsString()).getJSONArray("response").getJSONObject(0).getInt("friend_status") == 3;
        } catch (ClientException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer createChat(User user) {
        try {
            return vkApi.messages().createChat(botVk, getVkID(user)).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Integer getVkID(User user) {
        Optional<SocialNetwork> s = user.getSocialNetworks().stream().filter(socialNetwork ->
                socialNetwork.getSocialNetworkType().getName().equals("VK")).findFirst();
        int id;
        if (s.isPresent()) {
            id = Integer.parseInt(s.get().getLink());
        } else {
            throw new UserException("Не удалось получить Vk id пользователя");
        }
        return id;
    }

    @Override
    public String getAnotherSocialLinkFormat(String nickName) {
        try {
            return new JSONObject(vkApi.users().get(botVk).userIds(Collections.singletonList(nickName)).executeAsString()).getJSONObject("response").getString("link");
        } catch (ClientException | JSONException e) {
            e.printStackTrace();
        }
        //TODO Что за???
        return nickName;
    }

    @PostConstruct
    public void init() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vkApi = new VkApiClient(transportClient);
        botVk = new UserActor(vkBotId, vkBotAccessToken);
    }
}
