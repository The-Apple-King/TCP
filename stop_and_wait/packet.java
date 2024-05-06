public class packet {
    int seqnum;
    int acknum;
    int checksum;
    char []payload;
    // do not modify this
    public packet(){

    }
    // do not modify this
    public packet(int seqnum,int acknum,msg m){
        this.seqnum=seqnum;
        this.acknum=acknum;
        this.payload= new char[20];
        for (int i=0;i<20;i++) {
            payload[i]=m.data[i];
        }
        this.checksum=this.get_checksum();
    }
    // do not modify this
    public packet(packet pkt){
        this.seqnum=pkt.seqnum;
        this.acknum=pkt.acknum;
        this.checksum=pkt.checksum;
        this.payload= new char[20];

        for (int i=0;i<20;i++){
            this.payload[i]=pkt.payload[i];
        }
    }
    
    // do not modify this
    public packet(int acknum){
        this.acknum=acknum;
        this.payload= new char[20];
        for (int i=0;i<20;i++) {
            payload[i]=0;
        }
        this.checksum=get_checksum();
    }
    
    // do not modify this
    int get_checksum(){
        int checksum=0;
        for (int i=0;i<20;i++){
            checksum= checksum+(int)(this.payload[i]);
        }
        checksum=checksum+this.seqnum+this.acknum;
        return checksum;
    }
    
    static void send_ack(simulator sim,char AorB, int ack){
        packet pkt=new packet(ack);
        sim.to_layer_three(AorB,pkt);
    }
}
