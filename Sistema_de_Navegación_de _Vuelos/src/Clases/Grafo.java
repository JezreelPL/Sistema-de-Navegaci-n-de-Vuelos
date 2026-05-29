package Clases;

public class Grafo {

    public Nodos primero;

    public Grafo() {
        this.primero = null;
    }

    public String agregarAeropuerto(String codigo, String nombre, String pais) {
        if (buscarAeropuerto(codigo) != null) {
            return "Error: Ya existe un aeropuerto con codigo " + codigo;
        }

        Nodos nuevo = new Nodos(codigo, nombre, pais);

        if (primero == null) {
            primero = nuevo;
        } else {
            Nodos actual = primero;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        return "Aeropuerto '" + nombre + "' registrado correctamente.";
    }

    public Nodos buscarAeropuerto(String codigo) {
        Nodos actual = primero;
        while (actual != null) {
            if (actual.getCodigo().equalsIgnoreCase(codigo)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public String registrarVuelo(String codigoOrigen, String codigoDestino,
            double distancia, double precio,
            double tiempoVuelo, double tiempoEspera) {

        Nodos origen = buscarAeropuerto(codigoOrigen);
        Nodos destino = buscarAeropuerto(codigoDestino);

        if (origen == null || destino == null) {
            return "Error: Uno de los aeropuertos no existe.";
        }

        if (codigoOrigen.equalsIgnoreCase(codigoDestino)) {
            return "Error: El origen y destino no pueden ser el mismo.";
        }

        if (distancia <= 0 || precio <= 0 || tiempoVuelo <= 0) {
            return "Error: Distancia, precio y tiempo deben ser mayores a 0.";
        }

        agregarArista(origen, destino, distancia, precio, tiempoVuelo, tiempoEspera);
        agregarArista(destino, origen, distancia, precio, tiempoVuelo, tiempoEspera);

        return "Vuelo registrado: " + origen.getNombre()
                + " ↔ " + destino.getNombre()
                + " | " + distancia + " km"
                + " | Q" + precio
                + " | " + tiempoVuelo + " hrs";
    }

    private void agregarArista(Nodos origen, Nodos destino,
            double distancia, double precio,
            double tiempoVuelo, double tiempoEspera) {
        Aristas nueva = new Aristas(destino, distancia, precio,
                tiempoVuelo, tiempoEspera);

        if (origen.getListaAristas() == null) {
            origen.setListaAristas(nueva);
        } else {
            Aristas actual = origen.getListaAristas();
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nueva);
        }
    }

    public String mostrarConexiones() {
        if (primero == null) {
            return "Sin aeropuertos registrados.";
        }

        String texto = "";
        Nodos actual = primero;

        while (actual != null) {
            texto += "Aeropuerto: " + actual.toString() + "\n";
            Aristas arista = actual.getListaAristas();

            if (arista == null) {
                texto += "   Sin vuelos registrados\n";
            } else {
                while (arista != null) {
                    texto += "   → " + arista.toString() + "\n";
                    arista = arista.getSiguiente();
                }
            }
            texto += "\n";
            actual = actual.getSiguiente();
        }
        return texto;
    }

    private void limpiarVisitados() {
        Nodos actual = primero;
        while (actual != null) {
            actual.setVisitado(false);
            actual = actual.getSiguiente();
        }
    }
 
    public String encontrarRutas(String codigoOrigen, String codigoDestino) {
        Nodos origen = buscarAeropuerto(codigoOrigen);
        Nodos destino = buscarAeropuerto(codigoDestino);
        if (origen == null || destino == null) {
            return "Error: Uno de los aeropuertos no existe.";
        }
        limpiarVisitados();
        int[] contador = {0};
        String[] resultado = {""};
        encontrarRutasRec(origen, destino, "", 0, 0, 0, resultado, contador);
        if (contador[0] == 0) {
            return "No existe ruta entre los aeropuertos.";
        }
        return resultado[0];
    }

    private void encontrarRutasRec(Nodos actual, Nodos destino,
            String caminoActual,
            double distancia, double precio, double tiempo,
            String[] resultado, int[] contador) {
        actual.setVisitado(true);
        caminoActual += actual.getNombre();

        if (actual == destino) {
            contador[0]++;
            resultado[0] += "Ruta " + contador[0] + ":\n"
                    + caminoActual + "\n"
                    + "Distancia total: " + distancia + " km\n"
                    + "Precio total:    Q" + precio + "\n"
                    + "Tiempo total:    " + tiempo + " hrs\n\n";
        } else {
            Aristas arista = actual.getListaAristas();
            while (arista != null) {
                if (!arista.getDestino().isVisitado()) {
                    encontrarRutasRec(arista.getDestino(), destino,
                            caminoActual + " → ",
                            distancia + arista.getDistancia(),
                            precio + arista.getPrecio(),
                            tiempo + arista.getTiempoVuelo() + arista.getTiempoEspera(),
                            resultado, contador);
                }
                arista = arista.getSiguiente();
            }
        }
        actual.setVisitado(false);
    }

    public String caminoMasCorto(String codigoOrigen, String codigoDestino) {
        Nodos origen = buscarAeropuerto(codigoOrigen);
        Nodos destino = buscarAeropuerto(codigoDestino);

        if (origen == null || destino == null) {
            return "Error: Uno de los aeropuertos no existe.";
        }

        limpiarVisitados();

        String[] mejorCamino = {""};
        double[] mejorDistancia = {Double.MAX_VALUE};
        double[] mejorPrecio = {0};
        double[] mejorTiempo = {0};

        caminoMasCortoRec(origen, destino,
                "", 0, 0, 0,
                mejorCamino, mejorDistancia,
                mejorPrecio, mejorTiempo);

        if (mejorDistancia[0] == Double.MAX_VALUE) {
            return "No existe camino entre los aeropuertos.";
        }

        return "Camino mas corto:\n" + mejorCamino[0]
                + "\nDistancia total: " + mejorDistancia[0] + " km"
                + "\nPrecio total:    Q" + mejorPrecio[0]
                + "\nTiempo total:    " + mejorTiempo[0] + " hrs";
    }

    private void caminoMasCortoRec(Nodos actual, Nodos destino,
            String caminoActual, double distActual,
            double precioActual, double tiempoActual,
            String[] mejorCamino, double[] mejorDistancia,
            double[] mejorPrecio, double[] mejorTiempo) {

        actual.setVisitado(true);
        caminoActual += actual.getNombre();

        if (actual == destino) {
            if (distActual < mejorDistancia[0]) {
                mejorDistancia[0] = distActual;
                mejorCamino[0] = caminoActual;
                mejorPrecio[0] = precioActual;
                mejorTiempo[0] = tiempoActual;
            }
            actual.setVisitado(false);
            return;
        }

        Aristas arista = actual.getListaAristas();
        while (arista != null) {
            if (!arista.getDestino().isVisitado()) {
                caminoMasCortoRec(
                        arista.getDestino(), destino,
                        caminoActual + " → ",
                        distActual + arista.getDistancia(),
                        precioActual + arista.getPrecio(),
                        tiempoActual + arista.getTiempoVuelo() + arista.getTiempoEspera(),
                        mejorCamino, mejorDistancia,
                        mejorPrecio, mejorTiempo
                );
            }
            arista = arista.getSiguiente();
        }

        actual.setVisitado(false);
    }


    public String mostrarAeropuertos() {
        if (primero == null) {
            return "No hay aeropuertos registrados.";
        }
        String texto = "";
        Nodos actual = primero;
        while (actual != null) {
            texto += actual.toString() + "\n";
            actual = actual.getSiguiente();
        }
        return texto;
    }

    public boolean estaVacio() {
        return primero == null;
    }
}
