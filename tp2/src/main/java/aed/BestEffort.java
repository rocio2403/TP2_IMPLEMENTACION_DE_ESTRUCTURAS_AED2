package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class BestEffort {
   
    private Ciudad[] ciudades;  
    public  Heap<Traslado> heapRedituable; 
    public Heap<Traslado> heapAntiguo;    
    private ArrayList<Integer> mayorGanancia; 
    private  ArrayList<Integer> mayorPerdida;  
    private Heap<Ciudad> heapSuperavit;
    private  int mayorSuperavit;
    private int maxGanancia;
    private int maxPerdida;
    private int cantGanancia; 
    private int cantTraslados;


    
    public BestEffort(int cantCiudades, Traslado[] traslados) {
        this.ciudades = new Ciudad[cantCiudades];
        for(int i = 0;i<cantCiudades;i++){
            ciudades[i] = new Ciudad(i, 0, 0, 0);
        }

        //inicializo los heaps, con el constructor que toma un array.
        Comparator<Ciudad> comparadorPorSuperavit = Comparator.comparing(Ciudad::getSuperavit);
        this.heapSuperavit = new Heap<>(comparadorPorSuperavit, ciudades);//O(|C|)
        
        Comparator<Traslado> comparadorPorGN = Comparator.comparing(Traslado::getGananciaNeta);
        this.heapRedituable = new Heap<>(comparadorPorGN, traslados);

        Comparator<Traslado> timeStampComparar = Comparator.comparing(Traslado::getTimestamp).reversed();
        this.heapAntiguo = new Heap<>(timeStampComparar, traslados);
        
        //seteo posiciones iniciales.
        inicializarPosicionesHeaps();
        inicializarPosicionesSuperavit();
       
        this.mayorSuperavit = 0;
        this.mayorGanancia = new ArrayList<Integer>(); 
        this.mayorPerdida = new ArrayList<Integer>();
        
        this.maxGanancia = -1;
        this.maxPerdida = -1;

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

    ///====================================================================
    ///         SETEO DE POSICIONES
    /// =================================================================
    /// 
    private void inicializarPosicionesHeaps() {
        ArrayList<Traslado> elementosRedituable = this.heapRedituable.elementos();
        ArrayList<Traslado> elementosAntiguo = this.heapAntiguo.elementos();
       
        for (int i = 0; i < elementosRedituable.size(); i++) {
            Traslado t = elementosRedituable.get(i);
            t.setPosRedituable(i);
        }
    
        for (int i = 0; i < elementosAntiguo.size(); i++) {
            Traslado t = elementosAntiguo.get(i);
            t.setPosHeapAntiguo(i);
        }
    }
    
    private void inicializarPosicionesSuperavit(){
        ArrayList<Ciudad> elementosSuperavit = this.heapSuperavit.elementos();
        
            
        for (int i = 0; i < elementosSuperavit.size(); i++) {
            Ciudad c = elementosSuperavit.get(i);
            c.setPosHeapSuperavit(i);
        }
    }

    private void setearPosicionesRedituable(ArrayList<Integer> posiciones){
    for (int pos : posiciones) {
        Traslado traslado = heapRedituable.obtener(pos);
        traslado.setPosRedituable(pos);
    }
}

    private void setearPosicionesAntiguo(ArrayList<Integer> posiciones){
    for (int pos : posiciones) {
        Traslado traslado = heapAntiguo.obtener(pos);
        traslado.setPosHeapAntiguo(pos);
    }
}
    private void setearPosicionesSuperavit(ArrayList<Integer> posiciones){
    for (int pos : posiciones) {
        Ciudad ciudad = heapSuperavit.obtener(pos);
        ciudad.setPosHeapSuperavit(pos);
    }
}   


//ARREGLAR FUNCIONES DE SETEO SON REDUNDANTES
   //=======================================

   public int[] despacharMasRedituables(int n){
        int i = 0;
        int[] res = new int[n];  //O(n)
      //  int[] indices = new int[n];//O(n) inicializo la lista de los indices de los traslados que debo borrar
    ArrayList<Integer> indices = new ArrayList<>();
        while (i < n && heapRedituable.getCardinal() > 0){

            Tupla<Traslado,ArrayList<Integer>> info  = heapRedituable.desencolar();  //O(log(T))
            Traslado t = info.getPrimero();
            actualizarMayorGanancia(t);
            actualizarMayorPerdida(t);
            actualizarInfoCiudad(t);    //O(Log(C)).La complejidad proviene del superavit.  
            res[i] = t.getId();    
           indices.add(t.getPosAntiguo());
          // indices[i] =t.getPosAntiguo();
           setearPosicionesRedituable(info.getSegundo()); 
              
        //    actualizarListaGanancia(t); //los meti en actualizar info ciudades, al parecer el problema esta en que actualiza la info de la ciudad, pero es como sino, entonces no compara bien, cuando lo hago despues, no hay problema
        //    actualizarListaPerdida(t);
           i++;
            
        } //Complejidad bucle = O(n(log(T) + log(C)))
       
         sincronizarHeap(this.heapAntiguo,indices); //o(nlog(T))
        // actualizarListaGananciasYPerdidas(); //O(|C|) //DEBE SER CONSTANTE.ACA,USANDO ESTE, PASAN LOS TEST
    
        return res ;
       //Complejidad = O(n(Log(T)) + O(n(Log(T) + log(C))) =  O(n(logT + logC))
    }

    public int[] despacharMasAntiguos(int n){
        int i = 0;
        int[] res = new int[n]; 
        ArrayList<Integer> indices = new ArrayList<>();
        //int[] indices = new int[n];
        while (i< n && heapAntiguo.getCardinal() > 0){
           Tupla<Traslado,ArrayList<Integer>> info = heapAntiguo.desencolar();
            Traslado t  = info.getPrimero();// O(1)
            actualizarMayorGanancia(t);
            actualizarMayorPerdida(t);
            actualizarInfoCiudad(t);    //O(Log(C)) 
           
            indices.add(t.getPosRedituable());
          //  indices[i]=t.getPosRedituable();
            
            setearPosicionesAntiguo(info.getSegundo());
            
            res[i] = t.getId();    
             i++;
            
        }   //Complejidad bucle = O(n(log(T) + log(C)))
     
        sincronizarHeap(this.heapRedituable,indices);
      // actualizarListaGananciasYPerdidas(); //O(|C|)
       
        
        return res ;
    } //Complejidad = O(n(Log(T) + log(C))) =  O(n(logT + logC))

    
    
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

    //CAMBIO PRINCIPAL. EN VEZ DE RECORRER TODO, LO HAGO CADA VEZ QUE AGREGO UN TRASLADO Y MIRO SOLO LAS CIUDADES AFECTADAS POR ESE TRASLADO
    
    //=========================================================================================================
    /// actualizar Lista ganancias y perdidas me toma complejidad lineal, puedo hacer que todo sea operaciones O(1), en vez de ver todos, miro solo al que le aumento la ganancia y la perdida
    /// separo en dos funciones
    
  
    private  void actualizarInfoCiudad(Traslado t) {
        
        int indiceCiudadGana = t.getOrigen(); //O(1)
        int monto = t.getGananciaNeta();//O(1)
        int indiceCiudadPierde = t.getDestino();//O(1)
   
        this.ciudades[indiceCiudadGana].agregarGanancias(monto); //O(1)
        this.ciudades[indiceCiudadPierde].agregarPerdidas(monto);//O(1)

        cantGanancia += monto;//O(1)
        cantTraslados++;//O(1)     

        // actualizarListaGanancia(indiceCiudadGana); //ACA ESTA EL PROBLEMA
        // actualizarListaPerdida(indiceCiudadPierde);//ACA ESTA EL PROBLEMA

        ArrayList<Integer> cambios1 = heapSuperavit.modificarEnHeap(ciudades[indiceCiudadGana].getPosHeapSuperavit());//O(logC)
        ArrayList<Integer> cambios2 = heapSuperavit.modificarEnHeap(ciudades[indiceCiudadPierde].getPosHeapSuperavit());//O(logC)
        
        setearPosicionesSuperavit(cambios1);
        setearPosicionesSuperavit(cambios2); //segun debugguer setea bien
        
        maxSuperavit();

    } 

//ENCONTRE EL BUG, ACTUALIZO Y SIEMPRE PREGUNTO POR LE PRIMERO, pero a ese primero ya se le actualizo
//ESTE NO LOS USO, DESPUES DE DEBUGGUEAR SIGO
    private void actualizarListaPerdida(int indiceCiudad){
        //si esta vacia, no tengo con que compara entonces agrego
        int monto = this.ciudades[indiceCiudad].getPerdidas(); //es por lo que quiero ver si tengo que agregar o no
        if (mayorPerdida.isEmpty()) {
            mayorPerdida.add(indiceCiudad);
            return;
        }
        //aca separo en casos, si es mayor clear() O(1) y agrego, si es igual,agrego
        else if(monto > this.maxPerdida){
            this.mayorPerdida.clear();//O(1)
            this.mayorPerdida.add(indiceCiudad);
        }else if (monto == maxPerdida ) {
            this.mayorPerdida.add(indiceCiudad);
        }
        this.maxPerdida = this.ciudades[this.mayorPerdida.get(0)].getPerdidas();
    }
    
    private void actualizarListaGanancia(int indiceCiudad){
        int monto = this.ciudades[indiceCiudad].getGanancias();
      
        if (mayorGanancia.isEmpty()) {
            mayorGanancia.add(indiceCiudad);
            return;
        }
        //aca separo en casos, si es mayor clear() O(1) y agrego, si es igual,agrego
       else if(monto > this.maxGanancia){
            this.mayorGanancia.clear();//O(1)
            this.mayorGanancia.add(indiceCiudad);
        }else if (monto == this.maxGanancia ) {
            this.mayorGanancia.add(indiceCiudad);
        }
        this.maxGanancia = this.ciudades[this.mayorGanancia.get(0)].getGanancias();
       
    }

  
    private void sincronizarHeap(Heap<Traslado> heap,ArrayList<Integer> indices){ //int[] indices
        for (int index : indices) {
            heap.eliminarPorPosicion(index); 
        }
    }
    
 


    
//En caso de empate, devuelve la que tiene menor identificador
    private void maxSuperavit(){
        
        int idMaxS = this.mayorSuperavit; 
        int actualS = this.ciudades[idMaxS].getSuperavit();
        if( actualS < heapSuperavit.getMax().getSuperavit()){
            this.mayorSuperavit = heapSuperavit.getMax().getId();
        }
        else if(actualS == heapSuperavit.getMax().getSuperavit() 
            &&  heapSuperavit.getMax().getId() < idMaxS ){
                this.mayorSuperavit = heapSuperavit.getMax().getId();
        }
       
    //O(1)
    }

 


    //==============================================================
    // PRUEBO OTRO ENFOQUE
    private void actualizarMayorGanancia(Traslado traslado){ // Complejidad: O(1)
            int numeroDeCiudadOrigen = ciudades[traslado.getOrigen()].getId();
            int gananciaTotalOrigen = ciudades[traslado.getOrigen()].getGanancias() + traslado.getGananciaNeta();
           
            if (mayorGanancia.isEmpty() || gananciaTotalOrigen == ciudades[mayorGanancia.get(0)].getGanancias())    
                mayorGanancia.add(numeroDeCiudadOrigen);   
            else if (gananciaTotalOrigen > ciudades[mayorGanancia.get(0)].getGanancias()){                                  
                mayorGanancia.clear();
                mayorGanancia.add(numeroDeCiudadOrigen); 
            } 
        }
        
        private void actualizarMayorPerdida(Traslado traslado){  // Complejidad: O(1)
            
            int numeroDeCiudadDestino = ciudades[traslado.getDestino()].getId();
            int perdidaTotalDestino = ciudades[traslado.getDestino()].getPerdidas() + traslado.getGananciaNeta();
            if (mayorPerdida.isEmpty() || perdidaTotalDestino == ciudades[mayorPerdida.get(0)].getPerdidas())
                mayorPerdida.add(numeroDeCiudadDestino);
            else if (perdidaTotalDestino > ciudades[mayorPerdida.get(0)].getPerdidas()){
                mayorPerdida.clear();
                mayorPerdida.add(numeroDeCiudadDestino);
            } 
        }
    
  //   private void sincronizarHeapAntiguo(int[] indices){
    //     //en esta funcion le paso los elementos a borrar
    //     for(int index : indices){
    //         this.heapAntiguo.eliminarPorPosicion(index);

    //     }
    // }

    // private void sincronizarHeapRedituable(int[] indices){
    //     //en esta funcion le paso los elementos a borrar
    //     for(int index : indices){
    //         this.heapRedituable.eliminarPorPosicion(index);
    //     }
    // }
    //ANDA, LO USO COMO MODELO PARA ENCONTRAR ALGO QUE NO SEA LINEAL. ESTE FUNCIONA BIEN
//   public void actualizarListaGananciasYPerdidas() { 
//     int mayorGananciaActual = -1; 
//     int mayorPerdidaActual = -1;
//     mayorGanancia.clear();
//     mayorPerdida.clear();
//     if (!mayorGanancia.isEmpty()) {
//         int idCiudadGanancia = mayorGanancia.get(0);
//         mayorGananciaActual = ciudades[idCiudadGanancia].getGanancias();
//     }
//     if (!mayorPerdida.isEmpty()) {
//         int idCiudadPerdida = mayorPerdida.get(0);
//         mayorPerdidaActual = ciudades[idCiudadPerdida].getPerdidas();
//     }

//     for (Ciudad ciudad : ciudades) { // O(C)
//         int ganancia = ciudad.getGanancias();
//         if (ganancia > mayorGananciaActual) {
//             mayorGananciaActual = ganancia;
//             mayorGanancia.clear(); 
//             mayorGanancia.add(ciudad.getId());
//         } else if (ganancia == mayorGananciaActual && !mayorGanancia.contains(ciudad.getId())) {
            
//             mayorGanancia.add(ciudad.getId());
//         }

//         int perdida = ciudad.getPerdidas();
//         if (perdida > mayorPerdidaActual) {
//             mayorPerdidaActual = perdida;
//             mayorPerdida.clear(); 
//             mayorPerdida.add(ciudad.getId());
//         } else if (perdida == mayorPerdidaActual && !mayorPerdida.contains(ciudad.getId())) {
//             mayorPerdida.add(ciudad.getId());
//         }
//     }
// }

}    
    
        

