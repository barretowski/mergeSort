package Pilha;

public class Pilha {
    private NoPilha topo;

    public Pilha(){
        this.topo = null;
    }

    public boolean isEmpty(){
        return topo==null;
    }

    public void push(int info){
        NoPilha novo = new NoPilha(info);
        novo.setProx(topo);
        this.topo = novo;
    }

    public int pop(){
        if(!isEmpty()){
            int valor = topo.getInfo();
            this.topo = topo.getProx();
            return valor;
        }
        return -1;
    }


}
