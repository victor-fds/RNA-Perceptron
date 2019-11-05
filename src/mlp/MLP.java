package mlp;

import java.util.ArrayList;


public class MLP {
    private int numTreinamento=0;
    private double B;
    private double ni;
    private double erroDesejado;
    private double epocasMax;
    private int[] vetorAux;
    
    private ArrayList<Neuronio> rede = new ArrayList<>();

    /**
     * Cria a classe mpl
     * @param B
     * @param ni
     * @param erroDesejado
     * @param epocasMax 
     */
    public MLP(double B, double ni, double erroDesejado, double epocasMax) {
        this.B = B;
        this.ni = ni;
        this.erroDesejado = erroDesejado;
        this.epocasMax = epocasMax;
    }
    
    /**
     * Exibe a matriz de pesos
     */
    private void mostraMatriz(){
        for(int aux=0 ; aux<rede.size() ; aux++){
            
            if(rede.get(aux).getCamada() != 0){
                System.out.println("\n----------\n\nNeuronio("+aux+"):");
                rede.get(aux).MostraPeso();  
            }
        }
    }
    
    
    /**
     * Inicia uma rede baseada numa matriz de índices
     * @param neuronios 
     */
    public void iniciaRede(int[] neuronios){
        this.vetorAux = neuronios;
        int id=0;
        
        for(int i=0; i<neuronios.length; i++){
            for(int j=0; j<neuronios[i]; j++){
                //calcula a origem dos neuronios
                int[] origem = calculaOrigem(neuronios, i);
                
                //só calcula o destino dos neuronios que nao sao de saída
                int[] destino = calculaDestino(neuronios, i);
                
                Neuronio n = new Neuronio(id, destino, origem);
                n.setCamada(i);
                id++;
                
                rede.add(n);
            }
        }
    }
    
    
    /**
     * Calcula a origem dos neuronios (gera um vetor de indices da camada anterior)
     * @param neuronios
     * @param camadaAtual
     * @return 
     */
    private int[] calculaOrigem(int[] neuronios, int camadaAtual){
        int id=0;
        int[] aux;
        
        if(camadaAtual>0){
            //se não for neuronio sensorial, calcula os antecessores
            aux = new int[neuronios[camadaAtual-1]];
            
            for(int j=0; j<camadaAtual-1; j++){
                id += neuronios[j];
            }

            for(int i=0; i<aux.length; i++){
                aux[i] = id;
                id++;
            }
        } else
            aux = new int[1];

        
        return aux;
    }

    /**
     * Calcula a origem dos neuronios (gera um vetor de indices da camada posterior)
     * @param neuronios
     * @param camadaAtual
     * @return 
    */
    private int[] calculaDestino(int[] neuronios, int camadaAtual){
        int id=0;
        int[] aux;
        
        if(camadaAtual != neuronios.length-1){
            //se não for neuronio de saida, calcula os sucessores
            aux = new int[neuronios[camadaAtual+1]];
            
            for(int j=0; j<camadaAtual+1; j++){
                id += neuronios[j];
            }

            for(int i=0; i<aux.length; i++){
                aux[i] = id;
                id++;
            }
        } else
            aux = new int[1];

        return aux;
    }
        
    /**
     * Retorna o neurônio de saída
     * @return 
     */
    private Neuronio pegaNeuronioSaida(){
        return rede.get(rede.size()-1);
    }
                
    //------Fator de Ajuste (Saída)----------//
    /*
     * Realiza o calculo do ajuste dos 
     * @param saida
     * @param rede
     * @param desejado
     * @return 
    */
    private double gradienteSaida(Neuronio neuronio, double desejado){
        return neuronio.getY() * (1 - neuronio.getY()) * (desejado - neuronio.getY()); 
    }
    
    /**
     * Realiza o ajuste no neurônio de saída
     * @param saida
     * @param desejado
     * @return 
     */
    private double ajusteSaida(Neuronio saida, double desejado){
        double tt = saida.getPesoTeta();
        saida.setGrad(gradienteSaida(saida,desejado));
        
        for(int aux=0; aux < saida.getOrigem().length; aux++){
            Neuronio antecessor = rede.get(saida.getOrigem()[aux]);

            saida.getW()[0] += ni * antecessor.getY() * saida.getGrad();
        }
        
        tt += ni * tt * saida.getGrad();
        
        saida.setPesoTeta(tt);
        
        return saida.getY();
    }
    
    /**
     * Retorna o valor do ultimo neuronio subtraído do valor desejado
     * @param saida
     * @param desejado
     * @return 
     */
    private double pegaValFinal(Neuronio saida, double desejado){
        return desejado - saida.getY();
    }
    
    /**
     * Soma os gradientes na camada oculta
     * @param atual
     * @return 
     */
    private double somaGrad(Neuronio atual){
        double soma=0;
        for(int aux=0;aux<atual.getDestinos().length;aux++){
            soma +=(rede.get(atual.getDestinos()[aux]).getGrad()) * atual.getW()[aux];     
        }
        
        return soma;
    }    
    
    /**
     * Realiza o cálculo do gradiente do neurônio passado
     * @param atual
     * @return 
     */
    private double gradienteOculta(Neuronio atual){
        atual.setGrad(atual.getY() * (1-atual.getY()) * somaGrad(atual));
        return atual.getGrad();
    }
    
    /**
     * Realiza o ajuste do neurônio passado
     * @param atual 
     */
    private void ajusteOculta(Neuronio atual){
        double tt = atual.getPesoTeta();
        atual.setGrad(gradienteOculta(atual));      
        
        for(int aux=0; aux < atual.getOrigem().length; aux++){
            Neuronio antecessor = rede.get(atual.getOrigem()[aux]);
            
            for(int aux2 = 0; aux2 < antecessor.getDestinos().length; aux2++){
                if(antecessor.getDestinos()[aux2] == atual.getId()){
                    atual.getW()[aux] += ni * antecessor.getY() * atual.getGrad();
                }
            }
        }
        
        tt += ni * tt * atual.getGrad();
        
        atual.setPesoTeta(tt);
    }
    
    /**
     * Treina a MLP de acordo com a quantidade de malignos e benignos passados
     * @param malignos
     * @param benignos
     * @param banco 
     */
    public void executar(double[][] malignos, double[][] benignos, double[][] banco){
               
        mostraMatriz();
        
        long tempoInicio = System.currentTimeMillis();
        
        for(int i=0; i<malignos.length; i++){
            treinamento(malignos[i]); //faz o treinamento com a linha i
            treinamento(benignos[i]); //faz o treinamento com a linha i
        }
        long tempoFinal = System.currentTimeMillis();
        
        System.out.println("\n------------- Após o treinamento -------------");
        mostraMatriz();
        
        System.out.println("\nForam necessárias " + numTreinamento + " épocas para realizar o treinamento");
        System.out.println("A taxa de acerto foi de: " + calculaTxAcerto(malignos.length+1, banco)*100 + "%");
        System.out.println("O tempo utilizado no treinamento foi de: " + (tempoFinal-tempoInicio) + "ms");
        //System.out.printf("Saída desejada: %.8f / Obtido: %.8f\n", banco[0][5], Math.abs(testa(banco[0])); //teste unitátio
    }
    
    /**
     * Realiza o cálculo da taxa de acerto, de acordo com um arredondamento do neurônio de saída
     * @param inicio
     * @param banco
     * @return 
     */
    private double calculaTxAcerto(int inicio, double[][] banco){
        int total=0, acertos=0;
        
        for(int i=inicio; i<banco.length; i++){
            int result;
            double teste = testa(banco[i]);
            
            if(teste > -0.1 && teste < 0.009){
                //se o valor estiver próximo de 0, arredonda pra 0
                result = 0;
            } else if(teste < 1.4 && teste > 0.8){
                // se estiver próximo de 1, arredonda pra 1
                result = 1;
            } else {
                //se não for passível de classificar, arredonda do modo java
                result = (int) teste;
            }
            
            if(result == (int) banco[i][5]){
                acertos++;
                total++;
            } else
                total++;
        }
        
        return (double) acertos / total;
    }
    
    /**
     * Testa uma entrada na MLP
     * @param entrada
     * @return 
     */
    private double testa(double[] entrada){
        //preenche a camada sensorial com a entrada
        for(int i=0; i<vetorAux[0]; i++){
            rede.get(i).setY(entrada[i]);
            rede.get(i).setX(null);
        }
        
        //realiza o feedforward
        for(int i=vetorAux[0]; i<rede.size(); i++){
            rede.get(i).atualizaX(rede);
            rede.get(i).logistica(B);
        }
      
        return pegaValFinal(pegaNeuronioSaida(), entrada[5]);          
    }
    
    /**
     * Inicia o treinamento da MLP
     * @param entrada
     * @param erro
     * @param epocasMax
     */
    private void treinamento(double[] entrada){
        for(int i=0; i<vetorAux[0]; i++){
            rede.get(i).setY(entrada[i]);
            rede.get(i).setX(null);
        }
                       
        //compara se o valor de saída atingiu o desejado
        int max=0;
        double erroFinal = erroDesejado+1;
        double percentual = erroDesejado+1;
        
        while(percentual > erroDesejado && max <= epocasMax){
            for(int i=vetorAux[0]; i<rede.size(); i++){
                rede.get(i).atualizaX(rede);
                rede.get(i).logistica(B);
            }
                    
            //ajusta a saida
            erroFinal = ajusteSaida(pegaNeuronioSaida(), entrada[5]); 
            percentual = Math.abs(erroFinal - entrada[5]) / entrada[5];
            
           if(percentual > erroDesejado){
                for(int i=0; i<=pegaNeuronioSaida().getId(); i++){
                    //ajusta os pesos e o teta do neuronio exceto os sensoriais
                    if(rede.get(i).getCamada() != 0){
                        ajusteOculta(rede.get(i));
                    }
                }
            }
            
            max++;     
            numTreinamento++;
        }
    }
  
    public double getB() {
        return B;
    }

    public void setB(double B) {
        this.B = B;
    }

    public double getNi() {
        return ni;
    }

    public void setNi(double ni) {
        this.ni = ni;
    }

    public ArrayList<Neuronio> getRede() {
        return rede;
    }

    public void setRede(ArrayList<Neuronio> rede) {
        this.rede = rede;
    }
        
}
