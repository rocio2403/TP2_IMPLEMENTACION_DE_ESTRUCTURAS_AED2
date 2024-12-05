package aed;
/*Materia : Algoritmos y Estructuras de datos 2
 * Segundo cuatrimestre 2024 
 * Reentrega trabajo practico
 * Grupo BYMRO.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Heap<T> {
    private ArrayList<T> heap;
    private int cardinal; 
    private Comparator<T> comparator;

    // Constructor de heap vacio
    public Heap(Comparator<T> comparator) { 
        this.cardinal = 0;                  // O(1)   
        this.heap = new ArrayList<T>();     // O(1)
        this.comparator = comparator;   // O(1)
    } // Complejidad: O(1)
    
    // Constructor que toma una lista
    public Heap(Comparator<T> comparator, T[] lista) {
        this.cardinal = lista.length;
        this.heap = new ArrayList<T>();
        this.heap.addAll(Arrays.asList(lista)); //O(n)
        this.comparator = comparator;
        construirHeap(); //O(n)
    } //Complejidad O(n)
        

    public  ArrayList<Integer> construirHeap() { //https://youtu.be/C8IqJshhVbg?si=v8nW5lusxiqwLVGe Algoritmo de Floyd
        ArrayList<Integer> cambios = new ArrayList<>();
        for (int i = (cardinal - 1) / 2; i >= 0; i--) { //O(n)
            reheap(i,cambios);  
        }
        return cambios;
    }//complejidad O(n)

    /*Justificacion de la complejidad
     * Al construir el heap, se empieza ajustando desde los nodos internos, hasta la raiz(por eso el for comienza por el "ultimo").Si bien ajusta un nodo con reheap, puede tomar O(logn) en el peor caso, la mayoria de los nodos estan cerca de las hojas y casi no requieren ajuste, teniendo complejidad constante O(1),esto equilibra el trabajo total resultando en una complejidad final lineal O(n) y no O(nlogn)
     * los nodos que estan mas cerca de la raiz son pocos pero requieren mas trabajo, mientras que los nodos más cerca de las hojas son muchos pero requieren menos esfuerzo.
     */
  

    public  ArrayList<Integer> obtenerPrioridades(){
        ArrayList<Integer> posiciones = construirHeap(); //O(n)
        return posiciones;
    }//complejidad O(n)
    
    public ArrayList<T> elementos(){
        return this.heap;
    }//complejidad O(1)

    public T obtener(int posicion){   
        return heap.get(posicion);   
    }//complejidad O(1)
    public boolean isEmpty() { 
        return cardinal < 1;
    }//complejidad O(1)
    
    public T getPrimero() { 
        if (!isEmpty()) {
            return this.heap.get(0);
        }
        return null;
    }//complejidad O(1)
    
    public int getCardinal() { 
        return cardinal;
    }//complejidad O(1)

    public ArrayList<Integer> eliminarPorPosicion(int posicion) {
        ArrayList<Integer> cambios = new ArrayList<>();
        
        if (!esPosValida(posicion)) {
            return cambios; 
        }
        
        if (posicion == this.cardinal - 1) {
            borrarUltimo(); //O(1)
            return cambios; 
        }
    
        this.heap.set(posicion, this.heap.get(cardinal - 1));//O(1)
        cardinal--;//O(1)
        reheap(posicion, cambios); //O(log(n))
        this.heap.remove(cardinal);//O(1)
    
        return cambios;
    }//complejidad O(log(n))
    
    
   
    public void borrarUltimo(){
        this.heap.remove(cardinal-1);//O(1) porque solo borramos el ultimo,de acuerdo a las complejidades anexadas con el enunciado del trabajo practico
        this.cardinal--;
    }//complejidad O(1)
    
    public ArrayList<Integer> encolar(T elem) {
        ArrayList<Integer> cambios = new ArrayList<>(); //O(1)
        if (cardinal >= heap.size()) {
            heap.add(elem); 
        } else {
            heap.set(cardinal, elem); //o(1) 
        }
        siftUp(cardinal, cambios); //O(log(n)) 
        cardinal++;
        return cambios; 
    }//complejidad O(log(n))
    
    public Tupla<T,ArrayList<Integer>> desencolar() {
        ArrayList<Integer> cambios = new ArrayList<>();
        T raiz = null;
        if (this.cardinal > 1) {
            raiz = this.heap.get(0);
            this.heap.set(0, this.heap.get(cardinal - 1));
            this.heap.remove(cardinal-1);
            this.cardinal--;
            reheap(0,cambios);//O(log(n))
        }else if (this.cardinal > 0) {
            raiz = this.heap.get(0);
            this.heap.remove(cardinal-1);
            this.cardinal--;
           
        }
        Tupla<T,ArrayList<Integer>> res = new Tupla<>(raiz,cambios);
        return res; //retorno lista de cambios para no romper encapsulamiento 
    }///complejidad O(log(n))
    
    public void reheap(int elem,ArrayList<Integer> cambios) {
        boolean done = false;
        T temp = heap.get(elem);
        int hijo_izq = 2 * elem + 1;

        while (!done && hijo_izq < cardinal) {//O(log(n))
            int hijo_mas_grande = hijo_izq;
            int hijo_der = hijo_izq + 1;

            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_mas_grande)) > 0) {
                hijo_mas_grande = hijo_der;
            }

            if (comparator.compare(temp, this.heap.get(hijo_mas_grande)) < 0) {
                this.heap.set(elem, this.heap.get(hijo_mas_grande));
                cambios.add(elem);
              
                elem = hijo_mas_grande;
                hijo_izq = 2 * elem + 1;
            } else {
                done = true;
            }
        }

        this.heap.set(elem, temp);
        cambios.add(elem);
        
    }//complejidad O(log(n))

    
    public ArrayList<Integer> modificarEnHeap(int posicion) {
        
        ArrayList<Integer> cambios = new ArrayList<>();
        if (esPosValida(posicion)) { // Complejidad: O(1)
            T elemento = this.heap.get(posicion); // O(1)
            siftUp(posicion, cambios); // O(log n) 
            siftDown(posicion, cambios); // O(log n) 
        }
        return cambios; // Complejidad: O(1)
    }// Complejidad: O(log n)
    
    private void siftUp(int posicion, ArrayList<Integer> cambios) {
        //Proposito: Garantizar que el heap preserve la propiedad del heap mientras sube el nuevo elemento a su posicion correcta.
       
        int padre = (posicion - 1) / 2; // O(1)
        T elemento = this.heap.get(posicion); // O(1)
        while (posicion > 0 && comparator.compare(elemento, this.heap.get(padre)) > 0) { // O(log n)
            this.heap.set(posicion, this.heap.get(padre)); // O(1)
            cambios.add(posicion); // O(1)
            posicion = padre; // O(1)
            padre = (posicion - 1) / 2; // O(1)
        }
        this.heap.set(posicion, elemento); // O(1)
        cambios.add(posicion); // O(1)
    } // Complejidad: O(log n)
    
    private void siftDown(int posicion, ArrayList<Integer> cambios) {
        //Proposito: Asegurar que un elemento en una posicion inicial dada descienda hasta donde preserve la propiedad del heap.
        
        T elemento = this.heap.get(posicion); // O(1)
        int hijo_izq = 2 * posicion + 1; // O(1)
    
        while (hijo_izq < cardinal) { // O(log n)
            int hijo_mas_grande = hijo_izq; // O(1)
            int hijo_der = hijo_izq + 1; // O(1)
    
            if (hijo_der < cardinal && comparator.compare(this.heap.get(hijo_der), this.heap.get(hijo_izq)) > 0) { // O(1)
                hijo_mas_grande = hijo_der; // O(1)
            }
    
            if (comparator.compare(elemento, this.heap.get(hijo_mas_grande)) < 0) { // O(1)
                this.heap.set(posicion, this.heap.get(hijo_mas_grande)); // O(1)
                cambios.add(posicion); // O(1)
                posicion = hijo_mas_grande; // O(1)
                hijo_izq = 2 * posicion + 1; // O(1)
            } else {
                break; // O(1)
            }
        }
        this.heap.set(posicion, elemento); // O(1)
        cambios.add(posicion); // O(1)
    }// Complejidad: O(log n)
    
    private boolean esPosValida(int posicion) {
        return posicion >= 0 && posicion < cardinal;
    } //O(1)
    
    @Override
    public String toString() {
        if (cardinal == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cardinal; i++) {
            sb.append(this.heap.get(i));
            if (i < cardinal - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
   
