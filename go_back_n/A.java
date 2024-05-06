import java.util.ArrayDeque;
import java.util.Queue;

public class A {
    int seq; // nextseqnum
    int estimated_rtt;
    circular_buffer c_b;
    Queue<msg> buffer;

    public A() {
        /* go back n, the initialization of A
        // You can set the estimated_rtt to be 30, which is used as a parameter when you call start_timer
        // initialize the initial sequence number to 0.
        // you need to initialize the circular buffer, using "new circular_buffer(int max)". max is the number of the packets that the buffer can hold.
         */
        // Set the initial sequence number to 0
        seq = 0;

        // Set the estimated RTT to 30
        estimated_rtt = 30;

        // Initialize the circular buffer with a maximum capacity
        c_b = new circular_buffer(5);
        buffer = new ArrayDeque<msg>();
    }

    public void A_input(simulator sim, packet p) {
//        System.out.println("IN A");
//        System.out.println("*************************************");
//        System.out.println("we are receiving an ack");
//        System.out.println("ack: " + seq + ", recieved ack: " + p.acknum);
//        System.out.println("checksum given: " + p.checksum + ", checksum received: " + p.get_checksum());
//        System.out.println("c_b.read: " + c_b.read);
//        System.out.println();

        /* go back n, A_input
        // verify that the packet is not corrupted.
        // update the circular buffer according to the acknowledgement number using "pop()"
         */

        if(p.checksum == p.get_checksum()){
            if(seq == p.acknum){
                c_b.pop();
                if(!buffer.isEmpty()){
                    packet inorder = new packet(seq, seq, buffer.poll());
                    sim.to_layer_three('A', inorder);
                    c_b.push(inorder);
                    seq = (seq+1)%(c_b.max*2);
                }
                if(seq == c_b.read){
                    sim.envlist.remove_timer();
                }
                else{
                    sim.envlist.start_timer('A', estimated_rtt);
                }
            }
        }
    }

    public void A_output(simulator sim, msg m){
//        System.out.println("IN A");
//        System.out.println("*************************************");
//        System.out.println("we are receiving data to send as a packet");
//        System.out.println("seq: " + seq + ", c_b.count: " + c_b.count + " msg: " +m.data[0]);


        /* go back n, A_output
        // if the buffer is full, just drop the packet.
        // construct the packet. Make sure that the sequence number is correct.
        // send the packet and save it to the circular buffer using "push()" of circular_buffer
        // Set the timer using "sim.envlist.start_timer", and the time should be set to estimated_rtt. Make sure that there is only one timer started in the event list.
         */
        if(!c_b.isfull()){
            packet p = new packet(seq, seq, m);
            sim.to_layer_three('A', p);
            c_b.push(p);
            if (seq == c_b.read){
//                System.out.println("Started a timer");
                sim.envlist.start_timer('A', estimated_rtt);
            }
            seq = (seq+1)%(c_b.max*2);
        }
        else{
            System.out.println("msg put into buffer: " + m.data[0]);
            buffer.add(m);
        }


//        System.out.println();
    }
    public void A_handle_timer(simulator sim){
//        System.out.println("IN A");
//        System.out.println("*************************************");

        /* go back n, A_handle_timer
        // read all the sent packet that it is not acknowledged using "read_all()" of the circular buffer and resend them
        // If you need to resend packets, set a timer after that
         */
        sim.envlist.start_timer('A', estimated_rtt);
        packet[] p = c_b.read_all();
        for (int i = 0; i < c_b.count; i++){
            sim.to_layer_three('A', p[i]);
//            System.out.println("we need to resend packet: " + p[i].payload[0]);
        }
//        System.out.println();

    }
}
