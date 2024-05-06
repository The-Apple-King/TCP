public class event {
    float evtime;
    String  evtype;
    char eventity;
    packet pkt;
    event prev;
    event next;

    public event(float evtime,String  evtype, char eventity, packet pkt){
        this.evtime = evtime;
        this.evtype = evtype;
        this.eventity = eventity;
        this.pkt = pkt;
        prev = null;
        next = null;
    }
    public void print_self(){
        System.out.printf("Event time:%f , type: %s entity: %c\n",this.evtime, this.evtype, this.eventity);
    }
}
