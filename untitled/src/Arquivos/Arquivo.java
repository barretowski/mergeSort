package Arquivos;

import Pilha.Pilha;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.RandomAccess;

import static java.util.logging.Logger.global;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private final int max=1024;
    private int TL;

    public Arquivo(String nomearquivo)
    {
        try
        {
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e){ }
    }

    public void truncate(long pos) //desloca eof
    {
        try
        {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc){ }
    }

    //semelhante ao feof() da linguagem C
    //verifica se o ponteiro esta no <EOF> do arquivo
    public boolean eof()
    {
        boolean retorno = false;

        try
        {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e){ }

        return (retorno);
    }

    //insere um Registro no final do arquivo, passado por parâmetro
    public void inserirRegNoFinal(Registro reg)
    {
        seekArq(fileSize());    //ultimo byte
        reg.gravaNoArq(arquivo);
    }

    public void exibirArq()
    {
        Registro aux = new Registro();
        int i;

        seekArq(0);
        i = 0;

        while (!this.eof())
        {
            System.out.println("Posicao " + i);

            aux.leDoArq(arquivo);
            aux.exibirReg();
            i++;
        }
    }

    public void exibirUmRegistro(int pos)
    {
        Registro aux = new Registro();

        seekArq(pos);
        System.out.println("Posicao " + pos);

        aux.leDoArq(arquivo);
        aux.exibirReg();
    }

    public void seekArq(int pos)
    {
        try
        {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e){ }
    }

    public void leArq()
    {
        int codigo, idade;
        String nome;
        codigo = Entrada.leInteger("Digite o código");

        while (codigo != 0)
        {
            nome = Entrada.leString("Digite o nome");
            idade = Entrada.leInteger("Digite a idade");
            inserirRegNoFinal(new Registro(codigo, nome, idade));

            codigo = Entrada.leInteger("Digite o código");
        }
    }

    private int fileSize()
    {
        int tam = 0;

        try{
            tam = (int) (arquivo.length()/Registro.length());
        }catch(IOException e){}

        return (tam);
    }
    public RandomAccessFile getArquivo(){
        return arquivo;
    }
    public void setArquivo(RandomAccessFile arquivo){

        this.arquivo = arquivo;
    }


    private void preencherArquivo(){
        String[] nomes = {"Paulo","Flavio","Carlos","João"};
        Random rand = new Random();
        Registro reg;

        for(TL = 0; TL < max; TL++){
            String nome = nomes[rand.nextInt(nomes.length-1)];

            reg = new Registro(rand.nextInt(max), nome, rand.nextInt(90));

            seekArq(TL);
            reg.gravaNoArq(arquivo);
        }
    }
    public void copiaArquivo(Arquivo arq){
        Registro reg = new Registro();
        int tl = fileSize();

        seekArq(0);
        arq.seekArq(0);

        for(int i=0; i<tl; i++){
            reg.leDoArq(arquivo);
            reg.gravaNoArq(arq.getArquivo());
        }
    }
    public void fusao(Arquivo arq, int ini1, int fim1, int ini2, int fim2){
        int i = ini1, j=ini2, k=0;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();

        seekArq(i);
        reg1.leDoArq(arquivo);//lê o primeiro indice do bloco da esquerda
        seekArq(j);
        reg2.leDoArq(arquivo);//lê o primeiro indice do bloco da direita
        arq.seekArq(k);

        while(i<=fim1 && j<=fim2){
            if(reg1.getCodigo()<reg2.getCodigo()){
                reg1.gravaNoArq(arq.getArquivo());
                i++;
                seekArq(i);
                reg1.leDoArq(arquivo);
            }else{
                reg2.gravaNoArq(arq.getArquivo());
                j++;
                seekArq(j);
                reg2.leDoArq(arquivo);
            }
            k++;
            arq.seekArq(k);
        }
        seekArq(i);
        while(i<=fim1){
            reg1.leDoArq(arquivo);
            i++;
            seekArq(i);
            reg1.gravaNoArq(arq.getArquivo());
            k++;
        }
        seekArq(j);
        while (j <= fim2) {
            reg2.leDoArq(arquivo);
            j++;
            reg2.gravaNoArq(arq.getArquivo());
            k++;
        }

        for(i=0; i<k; i++){//passa todos as posições inseridas no vetor auxiliar
            seekArq(i);
            reg1.leDoArq(arq.getArquivo());
            seekArq(i+ini1);
            reg1.gravaNoArq(arquivo);
        }

    }
    public void mergeSort(){
        Pilha p1 = new Pilha();
        Pilha p2 = new Pilha();
        int dir=fileSize()-1, esq=0, meio=(dir+esq)/2;
        Arquivo arqAux = new Arquivo("aux.txt");
        copiaArquivo(arqAux);
        p1.push(0);
        p1.push(fileSize()-1);

        while(!p1.isEmpty()){
            dir = p1.pop();
            esq = p1.pop();
            meio = (dir+esq)/2;

            p1.push(esq);
            p1.push(meio);
            p2.push(esq);
            p2.push(meio);

            p1.push(meio+1);
            p1.push(dir);
            p2.push(meio+1);
            p2.push(dir);
        }
        int fim2, fim1, ini1, ini2;
        while(!p2.isEmpty()){
            fim2 = p2.pop();
            ini2 = p2.pop();
            fim1 = p2.pop();
            ini1 = p2.pop();

            fusao(arqAux, ini1, fim1, ini2, fim2);
        }
        exibirArq();

    }

    public void executar(){
        preencherArquivo();
        exibirArq();
    }
}
