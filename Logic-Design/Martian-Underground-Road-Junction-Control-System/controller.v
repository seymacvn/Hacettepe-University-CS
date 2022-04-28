`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 01.01.2020 17:20:25
// Design Name: 
// Module Name: controller
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////

module controller(
    input congestion,
    input clock,
    output reg[1:0] next_state
    );
    
   parameter s0 =2'b00, s1=2'b01, s2=2'b10, s3= 2'b11;
   
        //s0 represents GO for the main road, STOP for the side road
        //s1 represents GO-ATTENTION  for the main road, STOP-ATTENTION for the side road
        //s2 represents STOP for the main road, GO for the side road
        //s3 represents STOP-ATTENTION for the main road, GO-ATTENTION  for the side road
        
   reg[1:0] state = 2'b00;
   
   always@(posedge clock)
       state <= next_state;

   always@(state, congestion) begin
        case(state)
            s0:
                #20
                //defined 20ns for s0 state
                if(congestion == 0)
                    next_state = s1;
                    
                else if(congestion == 1)
                    next_state = s0;
                    
            s1: 
                #3
                //defined 3ns for s1 state
                if(congestion == 0) 
                    next_state= s2;
                else if(congestion == 1) 
                    next_state= s3;
            s2: 
                #10
                //defined 10ns for s2 state
                if(congestion == 0) 
                    next_state= s3;
                else if(congestion == 1) 
                    next_state= s3;
            s3: 
                #3
                //defined 3ns for s3 state
                if(congestion == 0) 
                    next_state= s0;
                else if(congestion == 1)
                    next_state= s0;
                    
            default: next_state = s0;
        endcase
    end
       
endmodule           