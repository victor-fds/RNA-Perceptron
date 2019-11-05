
package mamografia;

import mlp.MLP;
import normalizador.Formulas;

public class Mamografia {

    public static void main(String[] args) {

        int[] rede = {5, 3, 3, 1};  
        double beta = 1;
        double ni = 0.1;
        double txErro = 0.1;
        int epocasPorEntrada = 100;
        int tamanhoTreinamento = 100;
        
        
        
        MLP mlp = new MLP(beta, ni, txErro, epocasPorEntrada);
        mlp.iniciaRede(rede);
                
        //System.out.println("Neuronio: "+ mlp.getRede().get(3).getId() + "; Camada: " + mlp.getRede().get(3).getCamada());
        
        double[][] banco = new double[961][7];
        double[][] malignos = new double[tamanhoTreinamento][7];
        double[][] benignos = new double[tamanhoTreinamento][7];
        
        Util.carregaBanco(banco);
        
        if(Formulas.divideBanco(banco, malignos, benignos))
            System.out.println("Banco separado! São " + malignos.length + " malignos e " + benignos.length + " benignos");
 
        
        mlp.executar(malignos, benignos, banco);
    }
    
}
