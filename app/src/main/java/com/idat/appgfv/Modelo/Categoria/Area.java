package com.idat.appgfv.Modelo.Categoria;

public class Area {
    private Long id;
    private  String area;

    public Area() {
    }

    public Area(Long id, String area) {
        this.id = id;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
