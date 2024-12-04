package aed;
/*Materia : Algoritmos y Estructuras de datos 2
 * Segundo cuatrimestre 2024 
 * Reentrega trabajo practico
 * Grupo BYMRO.
 */

public class Traslado {
    
    private int id;
    private int origen;
    private int destino;
    private int gananciaNeta;
    private int timestamp;
    private int posHeapAntiguo; 
    private int posHeapRedituable;

    public Traslado(int id, int origen, int destino, int gananciaNeta, int timestamp){
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.gananciaNeta = gananciaNeta;
        this.timestamp = timestamp;
    }
    public int getId(){
        return id;
    }

    public int getOrigen(){
        return origen;
    }

    public int getDestino(){
        return destino;
    }
    public int getGananciaNeta() {
        return gananciaNeta;
    }

    public int getTimestamp() {
        return timestamp;
    }


    public int getPosRedituable(){
        return posHeapRedituable;
    }

    public int getPosAntiguo(){
        return posHeapAntiguo;
    }

    public void setPosRedituable(int posicionActualizada){
        this.posHeapRedituable = posicionActualizada;
    }

    public void setPosHeapAntiguo(int posicionActualizada){
        this.posHeapAntiguo = posicionActualizada;
    }

    
    @Override
    public String toString() {
    return "Traslado{" +
            "id=" + id +
            ", origen=" + origen +
            ", destino=" + destino +
            ", gananciaNeta=" + gananciaNeta +
            ", timestamp=" + timestamp +
            ", posHeapAntiguo=" + posHeapAntiguo +
            ", posHeapRedituable=" + posHeapRedituable +
            '}';
}


}
