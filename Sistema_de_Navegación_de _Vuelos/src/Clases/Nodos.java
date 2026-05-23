
package Clases;


public class Nodos {

    private String codigo;
    private String nombre;
    private String pais;

    // Apuntador al siguiente nodo
    private Nodos siguiente;

    // Lista de vuelos (aristas)
    private Aristas listaAristas;

    // Constructor
    public Nodos(String codigo, String nombre, String pais) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.siguiente = null;
        this.listaAristas = null;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Nodos getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodos siguiente) {
        this.siguiente = siguiente;
    }

    public Aristas getListaAristas() {
        return listaAristas;
    }

    public void setListaAristas(Aristas listaAristas) {
        this.listaAristas = listaAristas;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + pais + ")";
    }
}