package Pilha;

public class NoPilha {
    private NoPilha prox;
    private int info;


    public NoPilha(int info) {
        this.info = info;
        this.prox = null;
    }

    public void setProx(NoPilha prox){
        this.prox = prox;
    }
    public NoPilha getProx(){
        return prox;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getInfo(){
        return this.info;
    }
}
