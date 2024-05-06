import javax.lang.model.type.NullType;

public class event_list {
    event head;
    simulator sim;

    // do not modify this
    public event_list(simulator sim){
        this.head = null;
        this.sim= sim;
    }

    // do not modify this
    public void insert(event p){
        event q = head;
        if (q==null){
            head=p;
            head.next = null;
            head.prev = null;
        }
        else{
            event qold=q;
            while(q!=null && p.evtime>q.evtime){
                qold=q;
                q=q.next;
            }
            if(q==null){
                qold.next = p;
                p.prev = qold;
                p.next = null;
            }
            else{
                if(q==head){
                    p.next=q;
                    p.prev=null;
                    q.prev=p;
                    head=p;
                }
                else{
                    p.next=q;
                    p.prev=q.prev;
                    p.prev.next=p;
                    q.prev=p;
                }
            }
        }
    }

    // do not modify this
    public void print_self(){
        event q = head;
        System.out.println("--------------\nEvent List Follows:\n");
        while (q != null){
            System.out.printf("Event time:%.2f , type: %s entity: %c\n", q.evtime, q.evtype, q.eventity);
            q = q.next;
        }
        System.out.println("--------------\n");
    }

    // do not modify this
    public event remove_head(){
        event temp= this.head;
        if(temp==null){
            return null;
        }
        if(this.head.next==null){
            this.head=null;
            return temp;
        }
        else{
            this.head.next.prev=null;
            this.head=this.head.next;
            return temp;
        }
    }

    // you should call this method to start the timer
    // Make sure that there is only a timer at a time
    // do not modify this
    public void start_timer(char AorB, float time){
        this.insert(new event(sim.time +time,"TIMER_INTERRUPT",AorB,null));
    }

    // you should call this method to remove the timer
    // do not modify this
    public void remove_timer(){
        event q=this.head;

        while(q.evtype!="TIMER_INTERRUPT"){
            q=q.next;
        }
        if(q.prev==null){
            this.head=q.next;
        }
        else{
            if(q.next==null){
                q.prev.next=null;
            }
            else{
                q.next.prev=q.prev;
                q.prev.next=q.next;
            }
        }
    }
}