package mlp;

import java.util.ArrayList;
import java.util.Random;
import normalizador.Formulas;

public class Neuronio {
    
    private int id;
    private int camada;
    private double pesoTeta;
    private double grad;

    private int[] destinos;
    private double[] w;
    private double y;
    //Usar o índice como ponto de referencia;

    private int[] origem;
    private double[] x;
    //Usar o índice como ponto de referencia;
   
    
    //------------------------Construtores-----------------------//
    public Neuronio(int id, int[] destinos, int[] origem) {
        Random r = new Random();
        this.id = id;
        this.destinos = destinos;
        this.origem = origem;
        this.w = new double[origem.length];
        
        for(int i=0;i<origem.length;i++){

            this.w[i] = r.nextDouble();

            double num1 = r.nextDouble();

            this.pesoTeta = num1;
        }
        
        this.x = new double[origem.length];
    }
    
    public Neuronio() {
        
    }
    //--------------------------Métodos------------------------//
    
    //--------Função Logísitica---------//
    public double somatorio(Neuronio neuronio){
        double soma = 0;
        
        for(int aux = 0; aux < neuronio.origem.length; aux++){
            soma += neuronio.x[aux] * neuronio.w[aux] - pesoTeta;
        }
        
        return soma;
    }
    
    public void logistica(double B){
        double aux = 1 /(1 + Math.pow(Math.E, -B * somatorio(this)));
        this.y = Formulas.normalizaNumero(aux, 1, 0);
    }
    
     /*Atualiza valor X dos neuronios, exceto sensorial que recebe do arquivo*/
    public void atualizaX(ArrayList<Neuronio> rede){
        
        for(int aux=0; aux < this.getOrigem().length; aux++){
            Neuronio antecessor = rede.get(this.getOrigem()[aux]);

            this.x[aux] = antecessor.getY() ;
        }
        
    }
    
    /**
     * Mostra o valor do peso referente a este neurônio
     */
    public void MostraPeso(){
        for(int aux=0;aux<this.getOrigem().length;aux++){
            System.out.println("W["+this.origem[aux]+"]["+this.id+"] = "+this.w[aux]);
        }
        System.out.println("Tt["+this.id+"] = "+this.pesoTeta);     
    }
    //-------------------------Getters-------------------------//

    public int getCamada() {
        return camada;
    }

    public void setCamada(int camada) {
        this.camada = camada;
    }

       
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPesoTeta() {
        return pesoTeta;
    }

    public void setPesoTeta(double teta) {
        this.pesoTeta = teta;
    }

    public int[] getDestinos() {
        return destinos;
    }

    public void setDestinos(int[] destinos) {
        this.destinos = destinos;
    }

    public double[] getW() {
        return w;
    }

    public void setW(double[] w) {
        this.w = w;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int[] getOrigem() {
        return origem;
    }

    public void setOrigem(int[] origem) {
        this.origem = origem;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }
    
    public double getGrad() {
        return grad;
    }

    public void setGrad(double grad) {
        this.grad = grad;
    }
 
    
}