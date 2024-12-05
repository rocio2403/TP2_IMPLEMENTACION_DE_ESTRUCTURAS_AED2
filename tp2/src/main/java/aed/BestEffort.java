package aed;
/*Materia : Algoritmos y Estructuras de datos 2
 * Segundo cuatrimestre 2024 
 * Reentrega trabajo practico
 * Grupo BYMRO.
 */
import java.util.ArrayList;
import java.util.Comparator;

/*          ========== PRINCIPALES CAMBIOS DE LA REENTREGA ===============
 * Usamos un heap generico en lugar de discriminar entre maxHeap y minHeap
 * En la implementacion de heap usamos un arreglo redimensionable.
 * Para actualizar las posiciones, utilizamos la idea original presentada en el laboratorio: que las operaciones del heap devuelvan una lista de cambios = un historial de swaps,respetando asi el encapsulamiento.
 * 
 * Añadimos una clase tuplas.
 * 
 * Realizamos los test utilizando asserts
 * 
 * Actualizamos las listas de ganancias y perdidas en complejidad constante, el error anterior provenia de que siempre actualizabamos primero y preguntabamos la ganancia con los ids de la lista, siendo estos ya actualizados, por lo que siempre veia la ganancia/perdida actualizada y no comparaba bien.Por eso, realizamos esas operaciones previas a actualizar info de ciudades.
 * 
 * Descubrimos que el hecho de que no pasara el test de gananciaPromedioPorTraslado (considerando la nueva forma de actualizar posiciones) proviene de no haber considerado casos bordes, como eliminar el ultimo o desencolar cuando el heap tiene un solo elemento.
 * 
 * Dejamos comentada la complejidad en cada metodo.
 * 
 * Los test propios de la clase BestEffort se encuentran debajo de los proporcionados por la catedra.
 */



public class BestEffort {
   
    private Ciudad[] ciudades;  

    private Heap<Traslado> heapRedituable; 
    private Heap<Traslado> heapAntiguo;    
    private Heap<Ciudad> heapSuperavit;
    
    private ArrayList<Integer> mayorGanancia; 
    private  ArrayList<Integer> mayorPerdida;  
    
    private  int mayorSuperavit;
    private int cantGanancia; 
    private int cantTraslados;


    
    public BestEffort(int cantCiudades, Traslado[] traslados) {
        this.ciudades = new Ciudad[cantCiudades];
        for(int i = 0;i<cantCiudades;i++){
            ciudades[i] = new Ciudad(i, 0, 0, 0);
        } //O(|C|)

        //Costruimos los heaps utilizando el constructor que toma un arreglo
        Comparator<Ciudad> comparadorPorSuperavit = Comparator.comparing(Ciudad::getSuperavit);
        this.heapSuperavit = new Heap<>(comparadorPorSuperavit, ciudades);//O(|C|)
        
        Comparator<Traslado> comparadorPorGN = Comparator.comparing(Traslado::getGananciaNeta);
        this.heapRedituable = new Heap<>(comparadorPorGN, traslados); //O(|T|)

        Comparator<Traslado> timeStampComparar = Comparator.comparing(Traslado::getTimestamp).reversed();
        this.heapAntiguo = new Heap<>(timeStampComparar, traslados); //O(|T|)
        
        //seteamos posiciones iniciales
        inicializarPosicionesHeaps(); //O(|T|)
        inicializarPosicionesSuperavit(); //O(|C|)
       
        this.mayorSuperavit = 0;
        this.mayorGanancia = new ArrayList<Integer>(); 
        this.mayorPerdida = new ArrayList<Integer>();
      
        this.cantGanancia = 0;
        this.cantTraslados = 0;
       
    }// Complejidad final = O(|C|+ |T|)


    public void registrarTraslados(Traslado[] traslados){
        for (Traslado traslado : traslados){  //O(|traslados|)
            this.heapRedituable.encolar(traslado); //O(log(T))
            this.heapAntiguo.encolar(traslado); //O(log(T))
        } 
        inicializarPosicionesHeaps();  //O(|T|),donde |T| = |traslados|

    } //complejidad: max {O(|T|) +  O(|traslados|log(t))} = O(|traslados|log(t)))

    
   public int[] despacharMasRedituables(int n){
        int i = 0;
        int[] res = new int[n];  //O(n)
         while (i < n && heapRedituable.getCardinal() > 0){

            Tupla<Traslado,ArrayList<Integer>> info  = heapRedituable.desencolar();  //O(log(T))
            Traslado t = info.getPrimero();
            actualizarEstadisticas(t); //O(Log(C)).La complejidad proviene de actualizar el heap de superavit  
            res[i] = t.getId();    
           
           setearPosicionesRedituable(info.getSegundo()); //O(log(T)) ya que al eliminar un elemtno en un heap, hay a lo sumo log(t) cambios,pues un heap tiene altura logaritmica y se da como maximo un intercambio por nivel (hay Log(T) niveles)
          
           sincronizarAntiguo(t.getPosAntiguo()); //O(log(T))
           i++;
            
        } //Complejidad bucle = O(n(log(T) + log(C)))
        return res ;
       //Complejidad = O(n(Log(T) + log(C))) 
    }

    public int[] despacharMasAntiguos(int n){
        int i = 0;
        int[] res = new int[n]; 
         while (i< n && heapAntiguo.getCardinal() > 0){
           Tupla<Traslado,ArrayList<Integer>> info = heapAntiguo.desencolar();

            Traslado t  = info.getPrimero();// O(1)
           
            actualizarEstadisticas(t); //O(Log(C)).La complejidad proviene de actualizar el heap de superavit
        
            setearPosicionesAntiguo(info.getSegundo());//O(log(T)) ya que al eliminar un elemtno en un heap, hay a lo sumo log(t) cambios,pues un heap tiene altura logaritmica y se da como maximo un intercambio por nivel (hay Log(T) niveles)
            
            res[i] = t.getId();   //O(1) 
           
            sincronizarRedituable(t.getPosRedituable());//O(log(T))
           
            i++;//O(1)
            
        }   //Complejidad bucle = O(n(log(T) + log(C)))
      
     return res ;
    } //Complejidad  O(n(logT + logC))

    
    //Los siguientes metodos tienen complejidad constantes porque retornan variables privadas actualizadas con anterioridad, es decir,estan precalculadas.
    
    public int ciudadConMayorSuperavit(){
        return mayorSuperavit;//O(1)
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
         return mayorGanancia; //O(1)
        }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        
        return mayorPerdida;//O(1)
    }

    public int gananciaPromedioPorTraslado() {
        if (cantTraslados == 0) {
            return 0; 
        }
        return cantGanancia / cantTraslados; //O(1)
    }
    

    /* ====================================================================================
     *                  FUNCIONES AUXILIARES
     * ====================================================================================
    */


        /*==============================================================================
     *                  SETEO DE POSICIONES
     *==============================================================================
     */

     private void inicializarPosicionesHeaps() {
        ArrayList<Traslado> elementosRedituable = this.heapRedituable.elementos(); //O(1)
        ArrayList<Traslado> elementosAntiguo = this.heapAntiguo.elementos(); //O(1)
       
        for (int i = 0; i < elementosRedituable.size(); i++) { //O(|elementos|) donde |elementos| = T
            Traslado t = elementosRedituable.get(i);  //O(1)
            t.setPosRedituable(i);  //O(1)
        }
    
        for (int i = 0; i < elementosAntiguo.size(); i++) { //O(|elementos|) donde |elementos| = T
            Traslado t = elementosAntiguo.get(i); //O(1)
            t.setPosHeapAntiguo(i); //O(1)
        }
    } //Complejidad : O(T), T cantidad de elementos de los heaps, en ambos casos son iguales pues son la cantidad de traslados.
    
    private void inicializarPosicionesSuperavit(){
        ArrayList<Ciudad> elementosSuperavit = this.heapSuperavit.elementos(); //O(1)
        for (int i = 0; i < elementosSuperavit.size(); i++) { //O(|elementos|) donde |elementos| = C, c cantidad de ciudades
            Ciudad c = elementosSuperavit.get(i);//O(1)
            c.setPosHeapSuperavit(i);//O(1)
        }
    } //Complejidad O(|C|)


    private void setearPosicionesRedituable(ArrayList<Integer> posiciones){
    for (int pos : posiciones) { //O(|posiciones|)
        Traslado traslado = heapRedituable.obtener(pos); //O(1)
        traslado.setPosRedituable(pos); //O(1)
    }
  } //Complejidad : O(|posiciones|)
  //si bien tienen complejidad "lineal", en los metodos de despacharMasRedituables y despacharMasAntiguos, esta la justificacion de porque cumple la complejidad.

    private void setearPosicionesAntiguo(ArrayList<Integer> posiciones){
    for (int pos : posiciones) {//O(|posiciones|)
        Traslado traslado = heapAntiguo.obtener(pos);//O(1)
        traslado.setPosHeapAntiguo(pos);//O(1)
    }//Complejidad : O(|posiciones|)
  //si bien tienen complejidad "lineal", en los metodos de despacharMasRedituables y despacharMasAntiguos, esta la justificacion de porque cumple la complejidad.
}
    private void setearPosicionesSuperavit(ArrayList<Integer> posiciones){
        for (int pos : posiciones) {//O(|posiciones|)
            Ciudad ciudad = heapSuperavit.obtener(pos);//O(1)
            ciudad.setPosHeapSuperavit(pos);//O(1)
        }
}   //Complejidad : O(|posiciones|)
//si bien tienen complejidad "lineal", en los metodos de despacharMasRedituables y despacharMasAntiguos, esta la justificacion de porque cumple la complejidad.


/*======================================================================================
 *                  ACTUALIZACION DE ESTADISTICAS
 * =====================================================================================
 */

    private  void actualizarEstadisticas(Traslado t) {
        //actualizamos primero las ganancias y perdidas antes de actualizar la informacion de las ciudades.
        actualizarMayorGanancia(t); //O(1)
        actualizarMayorPerdida(t);//O(1)
        
        int indiceCiudadGana = t.getOrigen(); //O(1)
        int monto = t.getGananciaNeta();//O(1)
        int indiceCiudadPierde = t.getDestino();//O(1)
   
        this.ciudades[indiceCiudadGana].agregarGanancias(monto); //O(1)
        this.ciudades[indiceCiudadPierde].agregarPerdidas(monto);//O(1)

        cantGanancia += monto;//O(1)
        cantTraslados++;//O(1)     

        ArrayList<Integer> cambios1 = heapSuperavit.modificarEnHeap(ciudades[indiceCiudadGana].getPosHeapSuperavit());//O(logC)
        ArrayList<Integer> cambios2 = heapSuperavit.modificarEnHeap(ciudades[indiceCiudadPierde].getPosHeapSuperavit());//O(logC)
        
        setearPosicionesSuperavit(cambios1); //O(logC) 
        setearPosicionesSuperavit(cambios2); //O(logC) 
        
        maxSuperavit(); //O(1)

    } //Complejidad final =  O(logC)
     
    
//En caso de empate, devuelve la que tiene menor identificador
private void maxSuperavit(){
        
    int idMaxS = this.mayorSuperavit; //O(1)
    int actualS = this.ciudades[idMaxS].getSuperavit(); //O(1)
    if( actualS < heapSuperavit.getPrimero().getSuperavit()){ //O(1)
        this.mayorSuperavit = heapSuperavit.getPrimero().getId(); //O(1)
    }
    else if(actualS == heapSuperavit.getPrimero().getSuperavit()  //O(1)
        &&  heapSuperavit.getPrimero().getId() < idMaxS ){ //O(1)
            this.mayorSuperavit = heapSuperavit.getPrimero().getId(); //O(1)
    } 
}  //COMPLEJIDAD O(1)



private void actualizarMayorGanancia(Traslado traslado){ 
        int numeroDeCiudadOrigen = ciudades[traslado.getOrigen()].getId(); //O(1)
        int gananciaNueva = traslado.getGananciaNeta(); //O(1)
        int gananciaTotalOrigen = ciudades[traslado.getOrigen()].getGanancias() + gananciaNueva ; //O(1)
       
        if (mayorGanancia.isEmpty() || gananciaTotalOrigen == ciudades[mayorGanancia.get(0)].getGanancias())    //O(1)
          {  mayorGanancia.add(numeroDeCiudadOrigen);} //O(1)   
        else if (gananciaTotalOrigen > ciudades[mayorGanancia.get(0)].getGanancias()) {                                  
            mayorGanancia.clear(); //O(1)
            mayorGanancia.add(numeroDeCiudadOrigen);  //O(1)
        } 
    }// Complejidad  O(1)
    
private void actualizarMayorPerdida(Traslado traslado){  
        
        int numeroDeCiudadDestino = ciudades[traslado.getDestino()].getId();//O(1)
        int perdidaNueva = traslado.getGananciaNeta();//O(1)
        int perdidaTotalDestino = ciudades[traslado.getDestino()].getPerdidas() + perdidaNueva ;//O(1)
        if (mayorPerdida.isEmpty() || perdidaTotalDestino == ciudades[mayorPerdida.get(0)].getPerdidas())
           { mayorPerdida.add(numeroDeCiudadDestino);}//O(1)
        else if (perdidaTotalDestino > ciudades[mayorPerdida.get(0)].getPerdidas()){
            mayorPerdida.clear();
            mayorPerdida.add(numeroDeCiudadDestino);
        } //O(1)
    } // Complejidad O(1)


/*======================================================================================
 *                  SINCRONIZACION DE HEAPS
 * =====================================================================================
 */

   private void sincronizarRedituable( int pos){
            ArrayList<Integer> cambios = this.heapRedituable.eliminarPorPosicion(pos); //Log(T)
            if(cambios.size()!=0){
            setearPosicionesRedituable(cambios); //Log(T)
            }
    }//compeljidad O(logT)
    
    private void sincronizarAntiguo( int pos){
       
        ArrayList<Integer> cambios = this.heapAntiguo.eliminarPorPosicion(pos); //Log(T)
        if(cambios.size()!=0){
        setearPosicionesAntiguo(cambios); //Log(T)
        }
    }//complejidad O(logT)

  

}    
    
        

