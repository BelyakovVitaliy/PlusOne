package ru.project.plusone.model;

import java.util.regex.Pattern;

public class Program {
	public static void main(String[] args) {
		Pattern p = Pattern.compile("<\\/?\\w>");
		String g = "<p>Вы слышали о легендарном Анатолии Звереве? Это известный московский авангардист, наследию которого посвящена выставка «Зверев-GALA» в Музее AZ. В честь своего трёхлетия Музей AZ дарит уникальную возможность познакомиться с самым ярким нонконформистом и представителем субкультуры шестидесятников.</p>";

		System.out.println(p.matcher(g).replaceAll(""));
	}
}


