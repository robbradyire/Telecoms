/***
 * -------Classes----------
 * - Node 
 *	-> Need to implement
 * 	-> (Base provided)
 *
 * - Client
 *	-> Need to implement
 * 	-> (Base provided)
 *
 * - Server
 *	-> Need to implement
 * 	-> (Base provided)
 *
 * - PacketContent
 *	-> Need to implement
 * 	-> (Base provided)
 *
 * - WorkRequest
 *	-> Client side class
 *	-> Request includes how much work Client can currently handle (not fixed)
 *	-> On Server Side request indicates Clients workload doesn't contain target
 *
 * - Connection
 *	-> Need to implement
 *	-> Server and Client class
 *	-> Responsible for sending PingRequests and processing PingResponses and WorkRequests and SucessPacket
 *	-> Communicates to Server how big a WorkloadPacket to send and to which Client
 *
 * - Timer
 *	-> AbstractTimer Class added
 *	-> Look at end of file for example use
 *
 * - PingRequest
 *	-> Server side class
 *	-> Controlled in Connection class
 *	-> Sends a ping request to the Client
 *	-> Uses a Timer
 *
 * - PingResponse
 *	-> Need to implemet
 *	-> Client side class
 *	-> Controlled in Connection class
 *	-> Sends a response upon receipt of a PingRequest
 *
 * - WorkloadPacket
 *	-> need to implement
 *	-> Server side class
 *	-> Server assembles workload based on WorkRequest data
 *	-> Server sends WorkloadPacket with workload and other data
 *	-> Client takes WorkLoadPacket and processes it in ProcessData
 *
 * - ProcessData
 *	-> Need to implement
 *	-> Client side class
 *	-> Client takes relevent data from WorkloadPacket class
 *	-> Cleint scans workload for target
 *	-> Returns boolean based on sucess or failure of getting target
 *
 * - SucessPacket
 *	-> Need to implement
 *	-> Client side class
 *	-> If ProcessData suceeds in finding target Client sends SucessPacket to Server
 *	-> If Server receives SucessPacket sends TerminateWork message to all Clients.
 *	-> Server processes SucessPacket in Connection class
 *
 * - AcknowledgeReceipt
 *	-> Need to implement
 *	-> Client side class
 *	-> Client must acknowledge the receipt of WorkLoadPacket so that the Server doesn't resend it
 *
 * - Statistics
 *	-> Need to implement
 *	-> Server and Client sides class
 *	-> Maintains data on workload processed
 *	-> On the Server side, maintains data on who has done how much and is currently doing what etc
 *	-> On Client side, maintains data on how much individual has done, how much it is doing etc
 *
 * - TerminateWork
 *	-> Need to implement
 *	-> Client side class
 *	-> If Client finds target in Workload it must send
 *	-> Indicates to Server that no more work needs to be done
 *
 * ------------------------
 */