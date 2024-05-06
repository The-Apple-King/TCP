public class A {
    String state;
    int seq;
    int estimated_rtt;
    packet lastpacket;

    public A() {
        /* stop and wait, the initialization of A
        // for stop and wait, the state can be "WAIT_LAYER5" or "WAIT_ACK"
        // "WAIT_LAYER5" is the state that A waits messages from application layer.
        // "WAIT_ACK" is the state that A waits acknowledgement
        // You can set the estimated_rtt to be 30, which is used as a parameter when you call start_timer
         */

        state = "WAIT_LAYER5";
        seq = 0;
        estimated_rtt = 30;
    }

    public void A_input(simulator sim, packet p) {
        //System.out.println("IN A");
        //System.out.println("*************************************");
        //System.out.println("we are receiving an ack");
        //System.out.println("ack: " + seq + ", recieved ack: " + p.acknum);
        //System.out.println("checksum given: " + p.checksum + ", checksum received: " + p.get_checksum());
        //System.out.println("status: " + state);
        //System.out.println("last packet: " + lastpacket.payload[0]);
        //System.out.println();


        /* stop and wait A_input
        // the sim is the simulator. It is provided to call its method such as to_layer_three, start_timer and stop timer
        // p is the packet from the B
        // first verify the checksum to make sure that packet is uncorrupted
        // then verify the acknowledgement number to see whether it is the expected one
        // if not, you may need to resend the packet.
        // if the acknowledgement is the expected one, you need to update the state of A from "WAIT_ACK" to "WAIT_LAYER5" again
         */

        // check to see if we got a correct response
        if(p.checksum == p.get_checksum()) {
            if (seq == p.acknum) {
                //its correct we now update the state
                sim.envlist.remove_timer();
                state = "WAIT_LAYER5";
                seq = (seq+1)%2;
            }
        }
    }

    public void A_output(simulator sim, msg m) {
        //System.out.println("IN A");
        //System.out.println("*************************************");
        //System.out.println("we are receiving data to send as a packet");
        //System.out.println("seq: " + seq + " msg: " +m.data[0]);
        //System.out.println("status: " + state);
        //System.out.println();


        /* stop and wait A_output
        // the "sim" is the simulator. It is provided to call its method such as to_layer_three, start_timer and stop timer
        // msg m is the message. It should be used to construct the packet.
        // You can construct the packet using the "public packet(int seqnum,msg m)" in "packet.java".
        // save the packet so that it can be resent if needed.
        // Set the timer using "sim.envlist.start_timer", and the time should be set to estimated_rtt. Make sure that there is only one timer started in the event list.
        // In the end, you should change the state to "WAIT_ACK"
         */

        if(state.equals("WAIT_ACK")){
            System.out.println("missed packet: " + m.data[0]);
            return;
        }
        // create a new packet and update lastpacket in case we need to resend
        packet p = new packet(seq, seq, m);
        lastpacket = p;
        sim.to_layer_three('A', p);
        sim.envlist.start_timer('A', estimated_rtt);
        state = "WAIT_ACK";

    }

    public void A_handle_timer(simulator sim) {
        //System.out.println("IN A");
        //System.out.println("*************************************");
        //System.out.println("we need to resend a packet: " + lastpacket.payload[0]);
        //System.out.println("ack: " + seq + ", acknum: " + lastpacket.seqnum);
//        sim.envlist.print_self();
        //System.out.println();



        /* stop and wait A_handle_timer
        // the sim is the simulator. It is provided to call its method such as to_layer_three, start_timer and stop timer
        // if it is triggered, it means the previous sent packet isn't delivered
        // so you need to resend the last packet
        // After sending the last packet, don't forget to set the timer again
         */

        // we don't need to remove a timer it should have been done already
        //send last packet again and update state again
        sim.to_layer_three('A', lastpacket);
        sim.envlist.start_timer('A', estimated_rtt);
    }
}