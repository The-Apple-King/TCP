public class B {
    int seq;

    public B() {
        /* stop and wait, the initialization of B
        // The state only need to maintain the information of expected sequence number of packet
         */
        seq = 0;
    }

    public void B_input(simulator sim, packet p) {
        //System.out.println("IN B");
        //System.out.println("------------------------------------------");
        //System.out.println("we are getting a packet");
        //System.out.println("seq: " + seq + ", ack received: " + p.acknum);
        //System.out.println("checksum given: " + p.checksum + ", checksum received: " + p.get_checksum());
        //System.out.println("data: " + p.payload[0]);
        //System.out.println();
        /* stop and wait, B_input
        // you need to verify the checksum to make sure that packet isn't corrupted
        // If the packet is the right one, you need to pass to the fifth layer using "sim.to_layer_five(entity, payload)"
        // Send acknowledgement using "send_ack(sim, entity, seq)" of packet based on the correctness of received packet
        // If the packet is the correct one, in the last step, you need to update its state ( update the expected sequence number)
         */


        if(p.checksum == p.get_checksum()) {
            if (p.acknum == seq) { // if correct packet, send to layer 5 and update seq
                sim.to_layer_five('B', p.payload);
                packet.send_ack(sim, 'B', seq); // no matter what send ack
                seq = (seq+1)%2;


                //System.out.println("sent payload to layer 5 to print");
                //System.out.println("acknum: " + p.acknum + ", seq: " + seq);
                //System.out.println();
                return;
            }
        }
        //System.out.println("the ack or checksum didn't match");
        //System.out.println("acknum: " + p.acknum + ", seq" + ((seq+1)%2));
        //System.out.println();
        packet.send_ack(sim, 'B', ((seq+1)%2)); // no matter what send ack

    }

    public void B_output(simulator sim) {
        //System.out.println("This line shouldn't be reached in B_output");

    }

    public void B_handle_timer(simulator sim) {
        //System.out.println("This line shouldn't be reached in B_handle_timer");
    }
}