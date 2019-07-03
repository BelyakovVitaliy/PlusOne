package ru.project.plusone.model;

public class KudaGoLocation extends Location {
	private int radius;

	public KudaGoLocation(String address, double x, double y, int radius) {
		super(address, x, y);
		this.radius = radius;
	}
	public KudaGoLocation() {

	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}
