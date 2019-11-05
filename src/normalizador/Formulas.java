package normalizador;


public class Formulas {
    
    /**
     * Procura em todas as linhas e em todas as colunas se existe ?
     * Infere o dado de acordo com o laudo médico e a média da idade arredondado (int)
     * @param banco
     */
    public static void infereDados(double[][] banco){
       
        for(int coluna=0; coluna<5; coluna++){
            int mediaMenor=0;
            int quantMenor=0;
            
            int mediaMaior=0;
            int quantMaior=0;

            
            for(int i=0; i<banco.length; i++){
                //procura pela idade para calcular as médias
                if(banco[i][coluna] != -1){
                    if(banco[i][1] > 50){
                        mediaMaior += banco[i][coluna];
                        quantMaior++;
                    } 
                    
                    if(banco[i][coluna] <= 50){
                        mediaMenor += banco[i][coluna];
                        quantMenor++;
                    }
                }
            }

            mediaMaior = (int) mediaMaior/quantMaior;
            mediaMenor = (int) mediaMenor/quantMenor;

            for(int i=0; i<banco.length; i++){
                if(banco[i][coluna] == -1){
                    if(banco[i][5] == 1)
                        banco[i][coluna] = mediaMaior; //verifica se a severidade é 1 e insere a media maior
                     else
                        banco[i][coluna] = mediaMenor;
                }
            }
        }
    }
   
    /**
     * Separa a matriz em 
     * @param banco
     * @param malignos
     * @param benignos 
     * @return  
     */
    public static boolean divideBanco(double[][] banco, double[][] malignos, double[][] benignos){
        int qtdMalignos=0;
        int qtdBenignos=0;
        
        for(int i=0; i<961; i++){
            if(banco[i][5] == 1 && qtdMalignos < malignos.length){
                malignos[qtdMalignos] = banco[i];
                qtdMalignos++;
            } else if(banco[i][5] == 0 && qtdBenignos < benignos.length){
                benignos[qtdBenignos] = banco[i];
                qtdBenignos++;
            }
        }
        
        if(qtdMalignos == malignos.length && qtdBenignos == benignos.length)
            return true;
        else
            return false;
    }
    
    /**
     * Formaliza um numero para dentro do intervalo 0 a 1
     * @param x
     * @param maior
     * @param menor
     * @return 
     */
    public static double normalizaNumero(double x, double maior, double menor){
        return (x - menor) / (maior - menor);
    }
    
    public static void normaliza(double[][] banco){
        for(int i=0; i<banco.length; i++){
            for(int k=0; k<banco[0].length; k++){
                
                if(k == 0){
                    banco[i][k] = normalizaNumero(banco[i][k], 5, 1);
                } else if(k == 1){
                    banco[i][k] = normalizaNumero(banco[i][k], 96, 18);
                } else if(k == 2){
                    banco[i][k] = normalizaNumero(banco[i][k], 4, 1);
                } else if(k == 3){
                    banco[i][k] = normalizaNumero(banco[i][k], 5, 1);
                } else if(k == 4){
                    banco[i][k] = normalizaNumero(banco[i][k], 4, 1);
                }
                
            }
        }
    }
}
