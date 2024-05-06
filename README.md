Owen Swearingen, CSCI4211S232, 03/22/2023
Java, A.java & B.java, go_back_n.bat & stop_and_wait.bat


# Compilation and Execution 1
    I attempted to create a .bat file to compile and run each program in each folder.
    they still require you to have the necessary java stuff in order to run it

    both are in the main folder
    stop_and_wait.bat
    go_back_n.bat

    these should run the necessary commands and output the results to a terminal if not use the following 

# Compilation and Execution 2

## Compilation
    We assume that you have the required files to run java files

    Open a terminal and navigate to each folder based on which version you want to run
    
### ./stop_and_wait
    call javac .\simulator.java
### ./go_back_n
    Call javac .\simulator.java


## Execution
    Open up a terminal and navigete to the relative folder

    Run the following command

    $ java simulator

    the terminal will run the default test code from simulation

# description

## stop and wait
    on the sender side, A side
    when the a object is created instantiate the variables 
    state = "WAIT_LAYER5";  --wait for a packet to send
    seq = 0;                --sequence starts at 0
    estimated_rtt = 30;     --estimated time for a response

### A_input
    This function is for recieving acks from the other side

    1. Check for the correct packet, first checksum, then seq num.

    2a. If it is a correct packet we remove the timer, because it is correct, update the state to
    wait for another packet from simulator and increment the seq number.

    2b. If it is an incorrect packet we do nothing and allow the A_handle_timer to take care of it.

### A_output
    this function is for recieving messages from simulator and sending them to b

    1. First we check if the sender is available by checking the state. 
    
    2a. If the state is WAIT_ACK then print that a packet was dropped and print one 
    of the chars to keep track of the packets and return from the function.
    
    2b. If the state is WAIT_LAYER5 then the program can continue.

    3. We turn the msg into a packet and update lastpacket to store the new packet, 
    in case we need to retransmit.

    4. We send the packet to B and start a timer to let us know when a packet is dropped.
    
    5. Finally we update the state to WAIT_ACK to wait for a response.

### A_handle_timer
    This function is called when it takes too long to recieve a response from B, meaning that most likely a packet was dropped.

    1. First we need to resend the last sent packet saved in lastpacket.

    2. Then we start a new timer.

    Since the handle timer only gets called when a timer expires it has already been removed.
    

## go back n
    on the sender side, A side
    when the a object is created instantiate the variables 
    seq = 0;                        --sequence starts at 0
    estimated_rtt = 30;             --estimated time for a response
    c_b = new circular_buffer(5);   --our window for packets to send
    buffer = new ArrayDeque<msg>(); --a buffer to store packets we missed

### A_input
    Called when we recieve an ACK from B

    1. First check for the correct ACK, first checksum then acknum. 
    
    2a. If we fail either of these functions we do nothing and return from the function.

    2b. If we pass both of these tests then we remove the packet we recieved an ACK from, from 
    the window.

    3a. If we have a buffered message we pop it from the queue use it to create a new packet, send it to B and then push it onto the window. Finally update seq to match the packet num.

    4. If we have no buffered packets we check if the packet is the last in the window.

    5a. If the ack recieved was the correct ack for the end of the window we remove a timer.

    5b. If the ack recieved was any other packet in the window we start a new timer for the subsequent packets.

    
### A_output
    This function is called when simulator wants to send a packet to B

    1. First check if the window is full.

    2a. If the window is full print a message and add the packet to the buffer to be sent when we
    have space.

    2b. If the window isn't full create a new packet, send it to b, and save it to the window
    in case we need to resend the packet. 
    
    3. if the packet we added to the window is the only value in the window we start a new timer.

    4. finally increment seq in order to keep it updated

### A_handle_timer
    This function is called when we don't get a response from b in time, usually a dropped packet.

    We will be resending every packet in the window at once when this is called.

    1. Start a new timer, for the first packet in the window.

    2. Call c_b.read_all() to get every packet and save it to a packet array.

    3. Loop through every value in the Window and send it to b again

# B
## B on on both sides
    B is the reciever side 
    instantiate the seq to 0 at the start

### B_input
    This ic called when a packet is sent to B side.

    1. First check for the correct packet

    2a. If the packet is incorrect we call packet.send_ack() to send an ack to A with a seq number of the last in order packet received then return from the function

    2b. If the packet is correct we need to send the value back to the simulation in order to 
    print it out and show it was received.

    3. We then send an ACK to A confirming that we got the correct packet and update seq
        in stop_and_wait we alternate between 0 and 1
        int go_back_n we cycle through 2n values, this way we don't accidentally resend old 
        packets.
    and the return from the function.
    

# Evaluation

    I started by testing with no packet loss, first with a small number of packets then with a large number, both of those worked and printed out 
    a message of one character cycling through the alphabet.

    Then I tested with a .2 packet loss / corruption, enough to check if we
    can handle corruption or dropping a packet.
    When a packet gets corrupted my code checks every packet sent in for a
    checksum error to test for this, and if it detects a checksum error it 
    will throw out the packet.
    If we drop a packet then the sender will not receive a response before
    the timer runs out, therefor the sender will resend the packets and try 
    again. This process repeats until we get a response.

    Finally I tested with a .8 packet loss / corruption, the amount of
    errors make sending data take so long that we don't get all the data in time. 

    for stop_and_wait, we are sent a message to send while we are busy 
    sending a packet already. This means we need to drop the packet and 
    alert the simulation by printing out that a packet was dropped, it also 
    requires us to skip sending that packet.

    for go_back_n, I implemented a buffer so that missed packets can be sent at a later time, this means that packets are no longer dropped
    instead of being sent, however it creates a new problem. Since it takes
    so long to send packets we can run out of time to send packets so it can get cut off early. We can still track these because we print out
    when we send data to the buffer so we know what messages were given to
    A to send.





# Extra Credit
## rotating seq-num
    in order to accomplish this whenever we updated seq I added a "% (n*2)" this way the number doesn't go above the size of the window and loops 

## buffer 
    In order to accomplish this I created a queue, these have first in first out methods that will keep our packets in order
    we then push messages to this buffer when we don't have enough space in the circular buffer. 
    Then when we recieve an ack a spot opens up in the circular buffer and we create a packet with the msg inside the queue and remove that msg from the queue.
    we then increment the seq like we would if we just added a packet to the circular buffer.# TCP
