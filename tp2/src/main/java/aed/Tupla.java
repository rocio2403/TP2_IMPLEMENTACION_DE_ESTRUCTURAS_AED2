package aed;
/*Materia : Algoritmos y Estructuras de datos 2
 * Segundo cuatrimestre 2024 
 * Reentrega trabajo practico
 * Grupo BYMRO.
 */

 //Creamos la clase tupla para poder retornar el historial de swaps en los metodos de despachar traslados, para asi respetar el encapsulamiento de las clases :)

public class Tupla <T, M> {
    
    private T primero;
    private M segundo;

    public Tupla(T primero, M segundo) {
        this.primero = primero;
        this.segundo = segundo;
    }

    public T getPrimero() {
        return primero;
    }

    public M getSegundo() {
        return segundo;
    }
    
    public void setPrimero(T primero) {
        this.primero = primero;
    }

    public void setSegundo(M segundo) {
        this.segundo = segundo;
    }
} 