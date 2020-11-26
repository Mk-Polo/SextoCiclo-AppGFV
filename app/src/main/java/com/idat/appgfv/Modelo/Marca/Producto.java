package com.idat.appgfv.Modelo.Marca;

import android.hardware.Camera;

public class Producto {
private Long id;
private Camera.Area area;
private Categoria categoria;
private Marca marca;
private String producto;
private Long barra;
private String peso;
private int dias;
private String imagen;

    public Producto() {
    }

    public Producto(Long id, Camera.Area area, Categoria categoria, Marca marca, String producto, Long barra, String peso, int dias, String imagen) {
        this.id = id;
        this.area = area;
        this.categoria = categoria;
        this.marca = marca;
        this.producto = producto;
        this.barra = barra;
        this.peso = peso;
        this.dias = dias;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Camera.Area getArea() {
        return area;
    }

    public void setArea(Camera.Area area) {
        this.area = area;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Long getBarra() {
        return barra;
    }

    public void setBarra(Long barra) {
        this.barra = barra;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

