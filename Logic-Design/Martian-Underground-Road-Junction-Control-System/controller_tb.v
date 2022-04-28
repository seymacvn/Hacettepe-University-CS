`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 02.01.2020 01:31:23
// Design Name: 
// Module Name: controller_tb
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

`include "controller.v"
module controller_tb;
    reg  congestion;
    reg clock;
    wire[1:0] next_state;
    controller UUT( .congestion(congestion), .clock(clock), .next_state(next_state));
    
    initial begin
        congestion = 0; #105;
        congestion = 1; #20;
        congestion = 0; #200;
        $finish; 
    end 
    
    initial begin
        clock=1;
	    forever begin 
	       #0.01
	       clock=~clock;
	     end
	end
    
endmodule
