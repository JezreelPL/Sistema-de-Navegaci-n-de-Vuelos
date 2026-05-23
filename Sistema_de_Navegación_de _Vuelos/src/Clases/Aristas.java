
package Clases;


public class Aristas {

    // Nodo destino
    private Nodos destino;

    private double distancia;
    private double precio;
    private double tiempoVuelo;
    private double tiempoEspera;

    // Apuntador a la siguiente arista
    private Aristas siguiente;

    // Constructor
    public Aristas(Nodos destino, double distancia, double precio,double tiempoVuelo,double tiempoEspera) {

        this.destino = destino;
        this.distancia = distancia;
        this.precio = precio;
        this.tiempoVuelo = tiempoVuelo;
        this.tiempoEspera = tiempoEspera;
        this.siguiente = null;
    }

    // Getters y Setters
    public Nodos getDestino() {
        return destino;
    }

    public void setDestino(Nodos destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getTiempoVuelo() {
        return tiempoVuelo;
    }

    public void setTiempoVuelo(double tiempoVuelo) {
        this.tiempoVuelo = tiempoVuelo;
    }

    public double getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(double tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public Aristas getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Aristas siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return "Destino: " + destino.getNombre()
                + " | Distancia: " + distancia + " km"
                + " | Precio: Q" + precio
                + " | Tiempo vuelo: " + tiempoVuelo + " h"
                + " | Espera: " + tiempoEspera + " h";
    }
}