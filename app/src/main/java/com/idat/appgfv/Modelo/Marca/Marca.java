package com.idat.appgfv.Modelo.Marca;

public class Marca {

    private Long id;
    private String marca;

    public Marca() {
    }

    public Marca(Long id, String marca) {
        this.id = id;
        this.marca = marca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
