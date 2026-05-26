package Clases;


public class Grafo {

    public Nodos primero;

    public Grafo() {
        this.primero = null;
    }

    // AGREGAR AEROPUERTO
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

    // BUSCAR AEROPUERTO POR CODIGO
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

    
    // REGISTRAR VUELO
    public String registrarVuelo(String codigoOrigen, String codigoDestino,
                                  double distancia, double precio,
                                  double tiempoVuelo, double tiempoEspera) {

        Nodos origen  = buscarAeropuerto(codigoOrigen);
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

        return "Vuelo registrado: " + origen.getNombre() +
               " ↔ " + destino.getNombre() +
               " | " + distancia + " km" +
               " | Q" + precio +
               " | " + tiempoVuelo + " hrs";
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

    // MOSTRAR CONEXIONES
    public String mostrarConexiones() {
        if (primero == null) return "Sin aeropuertos registrados.";

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

    // LIMPIAR VISITADOS
    private void limpiarVisitados() {
        Nodos actual = primero;
        while (actual != null) {
            actual.setVisitado(false);
            actual = actual.getSiguiente();
        }
    }

    // ENCONTRAR RUTAS 
    public String encontrarRutas(String codigoOrigen, String codigoDestino) {
        Nodos origen  = buscarAeropuerto(codigoOrigen);
        Nodos destino = buscarAeropuerto(codigoDestino);

        if (origen == null || destino == null) {
            return "Error: Uno de los aeropuertos no existe.";
        }

        limpiarVisitados();
        String[] camino    = {""};
        double[] distancia = {0};
        double[] precio    = {0};
        double[] tiempo    = {0};

        boolean encontrado = encontrarRutasRec(origen, destino,
                                               camino, distancia,
                                               precio, tiempo);
        if (encontrado) {
            return "Ruta encontrada:\n" + camino[0] +
                   "\nDistancia total: " + distancia[0] + " km" +
                   "\nPrecio total:    Q" + precio[0] +
                   "\nTiempo total:    " + tiempo[0] + " hrs";
        }
        return "No existe ruta entre los aeropuertos.";
    }

    private boolean encontrarRutasRec(Nodos actual, Nodos destino,
                                       String[] camino, double[] distancia,
                                       double[] precio, double[] tiempo) {
        actual.setVisitado(true);
        camino[0] += actual.getNombre();

        if (actual == destino) {
            return true;
        }

        Aristas arista = actual.getListaAristas();
        while (arista != null) {
            if (!arista.getDestino().isVisitado()) {
                camino[0]    += " → ";
                distancia[0] += arista.getDistancia();
                precio[0]    += arista.getPrecio();
                tiempo[0]    += arista.getTiempoVuelo() + arista.getTiempoEspera();

                if (encontrarRutasRec(arista.getDestino(), destino,
                                      camino, distancia, precio, tiempo)) {
                    return true;
                }

                // Backtracking
                camino[0]    = camino[0].substring(0, camino[0].lastIndexOf(" → "));
                distancia[0] -= arista.getDistancia();
                precio[0]    -= arista.getPrecio();
                tiempo[0]    -= arista.getTiempoVuelo() + arista.getTiempoEspera();
            }
            arista = arista.getSiguiente();
        }
        return false;
    }

    
    // CAMINO MAS CORTO (por distancia)
    public String caminoMasCorto(String codigoOrigen, String codigoDestino) {
        Nodos origen  = buscarAeropuerto(codigoOrigen);
        Nodos destino = buscarAeropuerto(codigoDestino);

        if (origen == null || destino == null) {
            return "Error: Uno de los aeropuertos no existe.";
        }

        limpiarVisitados();

        String[] mejorCamino    = {""};
        double[] mejorDistancia = {Double.MAX_VALUE};
        double[] mejorPrecio    = {0};
        double[] mejorTiempo    = {0};

        caminoMasCortoRec(origen, destino,
                          "", 0, 0, 0,
                          mejorCamino, mejorDistancia,
                          mejorPrecio, mejorTiempo);

        if (mejorDistancia[0] == Double.MAX_VALUE) {
            return "No existe camino entre los aeropuertos.";
        }

        return "Camino mas corto:\n" + mejorCamino[0] +
               "\nDistancia total: " + mejorDistancia[0] + " km" +
               "\nPrecio total:    Q" + mejorPrecio[0] +
               "\nTiempo total:    " + mejorTiempo[0] + " hrs";
    }

    private void caminoMasCortoRec(Nodos actual, Nodos destino,
                                    String caminoActual, double distActual,
                                    double precioActual, double tiempoActual,
                                    String[] mejorCamino, double[] mejorDistancia,
                                    double[] mejorPrecio, double[] mejorTiempo) {

        actual.setVisitado(true);
        caminoActual += actual.getNombre();

        // Llegamos al destino
        if (actual == destino) {
            if (distActual < mejorDistancia[0]) {
                mejorDistancia[0] = distActual;
                mejorCamino[0]    = caminoActual;
                mejorPrecio[0]    = precioActual;
                mejorTiempo[0]    = tiempoActual;
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
                    distActual   + arista.getDistancia(),
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

    
    // MOSTRAR AEROPUERTOS
    public String mostrarAeropuertos() {
        if (primero == null) return "No hay aeropuertos registrados.";
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