package com.ncedu.fooddelivery.api.v1.dto;

public final class AboutDTO {

    private final String PROJECT = "food-delivery";
    private final String[] AUTHORS = {"Michael Dmitrichenko", "Michael Shchenev", "Alex Podshivalov"};
    private final String COACH = "Leonid Fedotov";

    public String getPROJECT() {
        return PROJECT;
    }

    public String[] getAUTHORS() {
        return AUTHORS;
    }

    public String getCOACH() {
        return COACH;
    }
}
