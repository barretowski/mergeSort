import Arquivos.Arquivo;

public class Main {
    public static void main(String[] args) {
        Arquivo arq = new Arquivo("novo");
        arq.executar();
        arq.mergeSort();

    }
}