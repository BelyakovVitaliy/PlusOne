package ru.project.plusone.model;

import javax.persistence.Embeddable;


@Embeddable
public class Location {


	private String address;

	private double x;

	private double y;

	public String toString() {
		return address;
	}

	public Location() {
	}

	public Location(String address, double x, double y) {
		this.address = address;
		this.x = x;
		this.y = y;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
