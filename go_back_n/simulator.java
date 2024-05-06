
import java.util.Random;

public class simulator {
    // Create a class constructor for the MyClass class
    int TRACE;
    int nsim;
    int nsimmax;
    float time;
    float lossprob;
    float corruptprob;
    int Lambda;
    int ntolayer3;
    int nlost;
    int ncorrupt;
    event_list envlist;
    A a;
    B b;

    // do not modify this
    public simulator() {
        // you may want to change the following parameters to adjust the characteristics of the communication channels
        lossprob= (float) 0.8; // probability that a packet is dropped .02 originally
        corruptprob= (float) 0.8; // probability that one bit is packet is flipped .02 originally
        Lambda=1000; // arrival rate of messages from layer 5
        nsimmax=30; // number of msgs to generate, then stop
        TRACE=0; // for your debugging

        nsim=0; // number of messages from 5 to 4 so far
        ntolayer3=0; // number sent into layer 3
        nlost=0; // number lost in media
        ncorrupt=0; // number corrupted by media
        time=0; // current time of the simulator

        envlist = new event_list(this);

        a= new A();
        b= new B();

        generate_next_arrival();
    }

    // do not modify this
    void generate_next_arrival(){
        float time = this.time + this.Lambda;
        this.envlist.insert(new event(time, "FROM_LAYER5", 'A',new packet()));
    }

    // do not modify this
    void run(){
        while(true){
            event env = this.envlist.remove_head();
            if (env == null) {
                System.out.println("simulation end!");
                return;
            }

            else {
                //env.print_self();
                this.time = env.evtime;
            }

            if(this.nsim == this.nsimmax) {
                System.out.println("The simulator has sent enough packets. Simulation end");
                return;
            }
            if (env.evtype == "FROM_LAYER5"){
                generate_next_arrival();
                char ch = (char)(97 + this.nsim % 26);
                msg m= new msg (ch);
                this.nsim=this.nsim+1;
                if (env.eventity == 'A'){
                    a.A_output(this,m);
                }
                else {
                    b.B_output(this);
                }
            }
            else{
                if(env.evtype=="FROM_LAYER3"){
                    packet pkt2give = env.pkt;
                    if(env.eventity == 'A'){
                        a.A_input(this, pkt2give);
                    }
                    else{
                        b.B_input(this,pkt2give);
                    }
                }
                else{
                    if(env.evtype=="TIMER_INTERRUPT"){
                        if(env.eventity == 'A'){
                            a.A_handle_timer(this);
                        }
                        else{
                            b.B_handle_timer(this);
                        }
                    }
                    else{
                        System.out.println("???????????");
                    }
                }
            }

        }
    }

    // do not modify this
    public static void main(String args[]){
        simulator sim = new simulator();
        sim.run();
    }

    // do not modify this
    public void to_layer_three(char eventity, packet p){
        Random rand = new Random(); //instance of random class
        int upperbound = 2;
        float float_random = rand.nextFloat();
        if (float_random< this.lossprob){
//            System.out.println("Drop the packet");
            return;
        }
        packet pkt = new packet(p);

        float_random = rand.nextFloat();
        if (float_random < this.corruptprob){
//            System.out.println("corrupted packet");
            pkt.payload[19]='*';
        }

        event q=this.envlist.head;
        float lasttime = this.time;
        while(q!=null){
            if(q.eventity==eventity && q.evtype == "FROM_LAYER3"){
                lasttime = q.evtime;
            }
            q=q.next;
        }
        float_random = rand.nextFloat();
        float eventime = lasttime + 1 + 9 * float_random;

        if (eventity=='A'){
            this.envlist.insert(new event(eventime,"FROM_LAYER3",'B',pkt));
        }
        else {
            this.envlist.insert(new event(eventime,"FROM_LAYER3",'A',pkt));
        }
    }
    public void to_layer_five(char eventity, char[] data){
        System.out.println("recieving " + new String(data));
    }
}
