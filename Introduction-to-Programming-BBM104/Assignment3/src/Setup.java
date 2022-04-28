
import java.util.ArrayList;

public class Setup {
	int k = 0;
    int addSquare(Square square1, Square square_list[]) {
    	square_list[k] = square1;
		k++;		
		return k;
	}
    int l = 0;
    int addPlayer(People player1, Player player_list[]) {
    	player_list[l] = (Player) player1;
		l++;		
		return l;
	}
	public void addBanker(People banker1, Banker banker_list[]) {
		banker_list[0] = ( Banker) banker1;
	}
	
	public void game(String player, int dice, Square square_list[], Player player_list[], Banker banker_list[], ArrayList chestList, ArrayList chanceList) {
		for(int i = 0; i < 2; i++) {
			if(player.equals(player_list[i].getName())){
				int k = (player_list[i].getNew_pos() + dice) ;
			    if(k > 40) {
					k = k % 40;
					player_list[i].setMoney(player_list[i].getMoney() + 200);
					banker_list[0].setMoney(banker_list[0].getMoney() - 200);
				}
			    else if (k < 0) {
			    	k = k + 20;
			    }
				if(player_list[i].getLocation().equals("Jail") && (player_list[i].getCounter() != 0)) {
					jail(player_list[i], player_list[1-i], player_list, dice);
				}
				
				else if(k <= 40) {
					player_list[i].setNew_pos(k);
					for(int j = 0; j < 40; j++) {
						if(square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("land") || square_list[j].getKind().equals("railroad") || square_list[j].getKind().equals("company"))) {
							player_list[i].setDice(dice);
							player_list[i].setLocation(square_list[j].getName());
							if(square_list[j].getStatus().contentEquals("empty")) {
								square_list[j].setStatus("full");
								if(player_list[i].getMoney() - square_list[j].getCost() >= 0) {
									player_list[i].setMoney(player_list[i].getMoney() - square_list[j].getCost());
									banker_list[0].setMoney(banker_list[0].getMoney() + square_list[j].getCost());
									player_list[i].getOwned().add(square_list[j].getName());
									player_list[i].getOwned_kind().add(square_list[j].getKind());
									System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" bought "+square_list[j].getName());
								}
								else {
									bankrupt(player_list[i], player_list[1-i], player_list, banker_list);
								}
							}
							else if(square_list[j].getStatus().contentEquals("full")){
								if(! player_list[i].getOwned().contains(square_list[j].getName())) {
									int rent =calculate_rent(square_list[j],player_list[i], player_list[1-i]);
									if(player_list[i].getMoney() - rent >= 0) {
										player_list[i].setMoney(player_list[i].getMoney() - rent);	
										player_list[1-i].setMoney(player_list[1-i].getMoney() + rent);
										System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" paid rent for "+square_list[j].getName());
									}
									else {
										bankrupt(player_list[i], player_list[1-i], player_list, banker_list);
									}
									}
								else {
									System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" has "+square_list[j].getName());
								}
							}
						}
						else if(square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("Community Chest") || square_list[j].getKind().equals("Chance"))) {
							player_list[i].setDice(dice);
							player_list[i].setLocation(square_list[j].getName());
							cards(chestList,chanceList, player_list[i], player_list[1-i], square_list[j], square_list, banker_list[0], dice);
						}
						else if(square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("Income Tax") || square_list[j].getKind().equals("Super Tax"))) {
							player_list[i].setDice(dice);
							player_list[i].setLocation(square_list[j].getName());
							tax(square_list[j], player_list[i], player_list[1-i], player_list, banker_list[0]);
							System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" paid Tax");
						}
						else if(square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("Free Parking"))) {
							player_list[i].setDice(dice);
							player_list[i].setLocation(square_list[j].getName());
							System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" is in Free Parking");
						}
						else if(square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("Go to Jail"))) {
							player_list[i].setDice(dice);
							player_list[i].setLocation("Jail");
							player_list[i].setCounter(1);
							player_list[i].setNew_pos(11);
							System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" went to jail");

						}
						else if(square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("Jail"))) {
							player_list[i].setDice(dice);
							jail(player_list[i], player_list[1-i], player_list, dice);
						}
						else if (square_list[j].getId() == player_list[i].getNew_pos() && (square_list[j].getKind().equals("GO"))){
							player_list[i].setDice(dice);
							player_list[i].setLocation("GO");
							player_list[i].setNew_pos(1);
							System.out.println(player_list[i].getName()+"\t"+ player_list[i].getDice()+"\t"+ player_list[i].getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player_list[i].getName()+" is in GO square");

						}
					}
				}
			}
		}
	}

	private void bankrupt(Player player1, Player player2, Player[] player_list, Banker[] banker_list) {
		System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player1.getName()+" goes bankrupt");
        Square square_list[] = new  Square[1];
		show(square_list, player_list, banker_list);
		System.exit(0);
	}
	private void jail(Player player1, Player player2, Player[] player_list, int dice) {
		player1.setDice(dice);
		player1.setLocation("Jail");
		player1.setNew_pos(11);
		if (player1.getCounter() == 0) {
			System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player1.getName()+" went to jail");
			player1.setCounter(player1.getCounter() + 1);
		}
		else if( player1.getCounter() < 4) {
			System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player_list[0].getMoney()+ "\t"+ player_list[1].getMoney()+"\t"+player1.getName()+" in jail (count="+ player1.getCounter()+")");
			player1.setCounter(player1.getCounter() + 1);
		}
		if( player1.getCounter() == 4) {
			player1.setCounter(0);
		}

	}

	private void tax(Square square, Player player, Player player2, Player[] player_list, Banker banker) {
		if(player.getMoney() - 100 >= 0) {
		player.setMoney(player.getMoney() - 100);
		banker.setMoney(banker.getMoney() + 100);
		}
		else {
			Banker[] banker_list = new Banker[1];
			banker_list[0]= banker;
			bankrupt(player, player2, player_list, banker_list);
		}
	}

	private void cards(ArrayList chestList, ArrayList chanceList, Player player1, Player player2, Square square,Square square_list[], Banker banker, int dice) {
		if(square.getKind().equals("Community Chest")) {
			String card = (String) chestList.get(0);
			chestList.add(card);
			chestList.remove(0);
			//System.out.println(card);
			cards1(card, player1, player2, square, banker);
		}
		else if(square.getKind().equals("Chance")) {
			String card = (String) chanceList.get(0);
			chanceList.add(card);
			chanceList.remove(0);
			cards2(card, player1, player2, square, banker, square_list, chestList, chanceList, dice);
		}
	}

	private void cards2(String card, Player player1, Player player2, Square square, Banker banker, Square square_list[], ArrayList chestList, ArrayList chanceList , int dice) {
		card = card.substring(1, card.length()-1);
		if(card.equals("Advance to Go (Collect $200)")){
			player1.setLocation("GO");
			player1.setMoney(player1.getMoney() + 200);
			banker.setMoney(banker.getMoney() - 200);
			player1.setNew_pos(1);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Advance to Go (Collect $200)");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Advance to Go (Collect $200)");
			}		
		}
		else if(card.equals("Pay poor tax of $15")) {
			if(player1.getMoney() - 15 >= 0) {
			player1.setMoney(player1.getMoney() - 15);
			banker.setMoney(banker.getMoney() + 15);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Pay poor tax of $15");
				}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Pay poor tax of $15");
				}
			}
			else {
				Player player_list1[] = new Player[2];
				if(player1.getName().equals("Player 1")) {
					player_list1[0]=player1;
					player_list1[1]=player2;
				}
				else {
					player_list1[0]=player2;
					player_list1[1]=player1;
				}
				Banker[] banker_list = new Banker[1];
				banker_list[0] = banker;
				bankrupt(player1, player2, player_list1, banker_list);
			}
		}
		else if(card.equals("Your building loan matures - collect $150")) {
			player1.setMoney(player1.getMoney() + 150);
			banker.setMoney(banker.getMoney() - 150);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Your building loan matures - collect $150");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Your building loan matures - collect $150");
			}
		}
		else if(card.equals("You have won a crossword competition - collect $100 ")) {
			player1.setMoney(player1.getMoney() + 100);
			banker.setMoney(banker.getMoney() - 100);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw You have won a crossword competition - collect $100 ");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw You have won a crossword competition - collect $100 ");
			}
		}
		else if(card.equals("Advance to Leicester Square")) {
			if(player1.getNew_pos() <= 27) {
				player1.setLocation("Leicester Square");
				player1.setNew_pos(27);
			}
			else {
				player1.setLocation("Leicester Square");
				player1.setNew_pos(27);
				player1.setMoney(player1.getMoney() + 200);
				banker.setMoney(banker.getMoney() - 200);
			}
			square(card, player1, player2, square, banker, square_list);
		}
		else if(card.equals("Go back 3 spaces")) {
			player1.setNew_pos(player1.getNew_pos() - 3);
			Player player_list1[] = new Player[2];
			if(player1.getName().equals("Player 1")) {
				player_list1[0]=player1;
				player_list1[1]=player2;
			}
			else {
				player_list1[0]=player2;
				player_list1[1]=player1;
			}
			game1(player1.getName(), dice, square_list, player_list1, banker, chestList, chanceList, player1, player2);
		}
	}

	private void game1(String name, int dice, Square[] square_list, Player[] player_list1, Banker banker,
			ArrayList chestList, ArrayList chanceList, Player player1, Player player2) {
				int k = player1.getNew_pos() ;
			    if(k > 40) {
					k = k % 40;
					player1.setMoney(player1.getMoney() + 200);
				}
			    else if (k < 0) {
			    	k = k + 40;
			    }
				if(player1.getLocation().equals("Jail") && (player1.getCounter() != 0)) {
					jail(player1, player2, player_list1, dice);
				}
				else if(k <= 40) {
					player1.setNew_pos(k);
					for(int j = 0; j < 40; j++) {
						if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("land") || square_list[j].getKind().equals("railroad") || square_list[j].getKind().equals("company"))) {
							player1.setLocation(square_list[j].getName());
							if(square_list[j].getStatus().contentEquals("empty")) {
								square_list[j].setStatus("full");
								if(player1.getMoney() - square_list[j].getCost() >= 0) {
								player1.setMoney(player1.getMoney() - square_list[j].getCost());
								banker.setMoney(banker.getMoney() + square_list[j].getCost());
								player1.getOwned().add(square_list[j].getName());
								player1.getOwned_kind().add(square_list[j].getKind());
								System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces" +player1.getName()+"bought "+square_list[j].getName());
								}
								else {
									Banker[] banker_list = new Banker[1];
									banker_list[0] = banker;
									bankrupt(player1, player2, player_list1, banker_list);
								}
							}
							else if(square_list[j].getStatus().contentEquals("full")){
								if(! player1.getOwned().contains(square_list[j].getName())) {
								int rent =calculate_rent(square_list[j],player1, player2);
								if(player1.getMoney() - rent >= 0) {
								player1.setMoney(player1.getMoney() - rent);	
								player2.setMoney(player2.getMoney() + rent);
								System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces "+player1.getName()+" paid rent for "+square_list[j].getName());
								}
								else {
									Banker[] banker_list = new Banker[1];
									banker_list[0] = banker;
									bankrupt(player1, player2, player_list1, banker_list);
									}
								}
								else {
									System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces " + player1.getName()+" has "+square_list[j].getName());
								}
							}
						}
						 else if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("Community Chest") || square_list[j].getKind().equals("Chance"))) {
							player1.setLocation(square_list[j].getName());
							player1.setNew_pos(square_list[j].getId());
							cards(chestList, chanceList, player1, player2, square_list[j], square_list, banker, dice);
						}
						else if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("Income Tax") || square_list[j].getKind().equals("Super Tax"))) {
							player1.setLocation(square_list[j].getName());
							tax(square_list[j], player1, player2,player_list1,banker);
							System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces "+ player1.getName()+" paid Tax");
							break;
						}
						else if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("Free Parking"))) {
							player1.setLocation(square_list[j].getName());
							System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces "+player_list1[1].getName()+" is in Free Parking");
						}
						else if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("Go to Jail"))) {
							player1.setLocation("Jail");
							player1.setCounter(1);
							player1.setNew_pos(11);
							System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces "+player1.getName()+" went to jail");

						}
						else if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("Jail"))) {
							player1.setLocation("Jail");
							player1.setNew_pos(11);
							if (player1.getCounter() == 0) {
								System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces "+player1.getName()+" went to jail");
								player1.setCounter(player1.getCounter() + 1);
							}
							else if( player1.getCounter() < 4) {
								System.out.println(player1.getName()+"\t"+ (player1.getDice())+"\t"+ player1.getNew_pos()+"\t"+ player_list1[0].getMoney()+ "\t"+ player_list1[1].getMoney()+"\t"+player1.getName()+" draw Go back 3 spaces "+player1.getName()+" in jail (count="+ player1.getCounter()+")");
								player1.setCounter(player1.getCounter() + 1);
							}
							if( player1.getCounter() == 4) {
								player1.setCounter(0);
							}
					   }
				 }
		  }
	}

	private void square(String card, Player player1, Player player2, Square square, Banker banker,
			Square[] square_list) {
		for(int j = 0; j < 40; j++) {
			if(square_list[j].getId() == player1.getNew_pos() && (square_list[j].getKind().equals("land") || square_list[j].getKind().equals("railroad") || square_list[j].getKind().equals("company"))) {
				player1.setLocation(square_list[j].getName());
				if(square_list[j].getStatus().contentEquals("empty")) {
					square_list[j].setStatus("full");
					if(player1.getMoney() - square_list[j].getCost() >= 0) {
					player1.setMoney(player1.getMoney() - square_list[j].getCost());
					banker.setMoney(banker.getMoney() + square_list[j].getCost());
					player1.getOwned().add(square_list[j].getName());
					player1.getOwned_kind().add(square_list[j].getKind());
					if(player1.getName().equals("Player 1")) {
						System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+"\t"+player2.getMoney()+"\t"+player1.getName()+" draw Advance to Leicester Square "+player1.getName()+" bought "+square_list[j].getName());
						}
					else if(player1.getName().equals("Player 2")) {
						System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+"\t"+player1.getMoney()+"\t"+player1.getName()+" draw Advance to Leicester Square "+player1.getName()+" bought "+square_list[j].getName());
						}	
					}
				else {
					Player player_list1[] = new Player[2];
					if(player1.getName().equals("Player 1")) {
						player_list1[0]=player1;
						player_list1[1]=player2;
					}
					else {
						player_list1[0]=player2;
						player_list1[1]=player1;
						}
					Banker[] banker_list = new Banker[1];
					banker_list[0] = banker;
					bankrupt(player1, player2, player_list1, banker_list);
					}
				}
				else if(square_list[j].getStatus().contentEquals("full")){
					if(! player1.getOwned().contains(square_list[j].getName())) {
					int rent =calculate_rent(square_list[j],player1, player2);
					if((player1.getMoney() - rent) >= 0) {
					player1.setMoney(player1.getMoney() - rent);	
					player2.setMoney(player2.getMoney() + rent);
					if(player1.getName().equals("Player 1")) {
						System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+"\t"+player2.getMoney()+"\t"+player1.getName()+" draw Advance to Leicester Square "+player1.getName()+" paid rent for "+square_list[j].getName());
						}
					else if(player1.getName().equals("Player 2")) {
						System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+"\t"+player1.getMoney()+"\t"+player1.getName()+" draw Advance to Leicester Square "+player1.getName()+" paid rent for "+square_list[j].getName());
						}	
					  }
					else {
						Player player_list1[] = new Player[2];
						if(player1.getName().equals("Player 1")) {
							player_list1[0]=player1;
							player_list1[1]=player2;
						}
						else {
							player_list1[0]=player2;
							player_list1[1]=player1;
							}
						 Banker[] banker_list = new Banker[1];
						 banker_list[0] = banker;
						 bankrupt(player1, player2, player_list1, banker_list);
						}
					}
					else {
						if(player1.getName().equals("Player 1")) {
							System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+"\t"+player2.getMoney()+"\t"+ player1.getName()+" draw "
									+ " to Leicester Square "+player1.getName()+" has "+square_list[j].getName());
							}
						else if(player1.getName().equals("Player 2")) {
							System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+"\t"+player1.getMoney()+"\t"+ player1.getName()+" draw Advance to Leicester Square "+player1.getName()+" has "+square_list[j].getName());
							}	
					}
				}
			}
		}
	}

	private void cards1(String card, Player player1, Player player2, Square square, Banker banker) {
		card = card.substring(1, card.length()-1);
		if(card.equals("Advance to Go (Collect $200)")){
			player1.setLocation("GO");
			player1.setMoney(player1.getMoney() + 200);
			banker.setMoney(banker.getMoney() - 200);
			player1.setNew_pos(1);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Advance to Go (Collect $200)");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Advance to Go (Collect $200)");
			}		
		}
		else if(card.equals("Bank error in your favor - collect $75")) {
			player1.setMoney(player1.getMoney() + 75);
			banker.setMoney(banker.getMoney() - 75);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Bank error in your favor - collect $75");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Bank error in your favor - collect $75");
			}
		}
		else if (card.equals("Doctor's fees - Pay $50")) {
			if(player1.getMoney() - 50 >= 0) {
			player1.setMoney(player1.getMoney() - 50);
			banker.setMoney(banker.getMoney() + 50);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Doctor's fees - Pay $50");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Doctor's fees - Pay $50");
				}
			}
			else{
				Player player_list1[] = new Player[2];
				if(player1.getName().equals("Player 1")) {
					player_list1[0]=player1;
					player_list1[1]=player2;
				}
				else {
					player_list1[0]=player2;
					player_list1[1]=player1;
				}
				Banker[] banker_list = new Banker[1];
				banker_list[0] = banker;
				bankrupt(player1, player2, player_list1, banker_list);
			}
		}
		else if(card.equals("It is your birthday Collect $10 from each player")) {
			player1.setMoney(player1.getMoney() + 10);
			player2.setMoney(player2.getMoney() - 10);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest It is your birthday Collect $10 from each player");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest It is your birthday Collect $10 from each player");
			}
		}
		else if(card.equals("Grand Opera Night - collect $50 from every player for opening night seats")) {
			player1.setMoney(player1.getMoney() + 50);
			player2.setMoney(player2.getMoney() - 50);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Grand Opera Night - collect $50 from every player for opening night seats");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Grand Opera Night - collect $50 from every player for opening night seats");
			}
		}
		else if(card.equals("Income Tax refund - collect $20")) {
			player1.setMoney(player1.getMoney() + 20);
			banker.setMoney(banker.getMoney() - 20);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Income Tax refund - collect $20");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Income Tax refund - collect $20");
			}
		}
		else if(card.equals("Life Insurance Matures - collect $100")) {
			player1.setMoney(player1.getMoney() + 100);
			banker.setMoney(banker.getMoney() - 100);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Life Insurance Matures - collect $100");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Life Insurance Matures - collect $100");
			}
		}
		else if(card.equals("Pay Hospital Fees of $100")) {
			if(player1.getMoney() - 100 >= 0) {
			player1.setMoney(player1.getMoney() - 100);
			banker.setMoney(banker.getMoney() + 100);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Pay Hospital Fees of $100");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Pay Hospital Fees of $100");
				}
			}
			else{
				Player player_list1[] = new Player[2];
				if(player1.getName().equals("Player 1")) {
					player_list1[0]=player1;
					player_list1[1]=player2;
				}
				else {
					player_list1[0]=player2;
					player_list1[1]=player1;
				}
				Banker[] banker_list = new Banker[1];
				banker_list[0] = banker;
				bankrupt(player1, player2, player_list1, banker_list);
			}
		}
		else if(card.equals("Pay School Fees of $50")) {
			if(player1.getMoney() - 50 >= 0) {
			player1.setMoney(player1.getMoney() - 50);
			banker.setMoney(banker.getMoney() + 50);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest Pay School Fees of $50");
				}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest Pay School Fees of $50");
				}
			}
			else{
				Player player_list1[] = new Player[2];
				if(player1.getName().equals("Player 1")) {
					player_list1[0]=player1;
					player_list1[1]=player2;
				}
				else {
					player_list1[0]=player2;
					player_list1[1]=player1;
				}
				Banker[] banker_list = new Banker[1];
				banker_list[0] = banker;
				bankrupt(player1, player2, player_list1, banker_list);
			}
		}
		else if(card.equals("You inherit $100")) {
			player1.setMoney(player1.getMoney() + 100);
			banker.setMoney(banker.getMoney() - 100);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest You inherit $100");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest You inherit $100");
			}
		}
		else if(card.equals("From sale of stock you get $50")) {
			player1.setMoney(player1.getMoney() + 50);
			banker.setMoney(banker.getMoney() - 50);
			if(player1.getName().equals("Player 1")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player1.getMoney()+ "\t"+ player2.getMoney()+"\t"+player1.getName()+" draw Community Chest From sale of stock you get $50");
			}
			else if(player1.getName().equals("Player 2")) {
				System.out.println(player1.getName()+"\t"+ player1.getDice()+"\t"+ player1.getNew_pos()+"\t"+ player2.getMoney()+ "\t"+ player1.getMoney()+"\t"+player1.getName()+" draw Community Chest From sale of stock you get $50");
			}
		}
	}

	private int calculate_rent(Square square, Player player1, Player player2) {
		int rent = 0;
		if(square.getKind().equals("land")) {
			if(square.getCost() <= 2000) {
				rent = (square.getCost()*40) / 100;

			}
			else if(square.getCost() <= 3000) {
				rent = (square.getCost()*30) / 100;
			}
			else if(square.getCost() <= 4000) {
				rent = (square.getCost()*35) / 100;
			}
		}
		else if(square.getKind().equals("railroad")) {
			int x = 0;
			for(int i = 0 ; i < player2.getOwned_kind().size(); i++) {
				if(player2.getOwned_kind().get(i).equals("railroad")) {
					x++;
				}
			}
			rent =( 25 * (x));
		}
		
		else if(square.getKind().equals("company")) {
			rent = (4 * player1.getDice());
		}
		return rent ;
	}
	

	public void show(Square[] square_list, Player[] player_list, Banker[] banker_list) {
		System.out.println("-----------------------------------------------------------------------------------------------------------");
		System.out.print(player_list[0].getName()+"\t"+ player_list[0].getMoney()+"\t"+"have: ");
		if(player_list[0].getOwned().size() == 0) {
			System.out.print("\n");
		}
		else {
		for(int i = 0 ; i < player_list[0].getOwned().size(); i ++) {
			if(i == player_list[0].getOwned().size() -1) {
				System.out.print(player_list[0].getOwned().get(i)+"\n");
			}
			else {
				System.out.print(player_list[0].getOwned().get(i)+",");
				}
			}
		}
		System.out.print(player_list[1].getName()+"\t"+ player_list[1].getMoney()+"\t"+"have: ");
		if(player_list[1].getOwned().size() == 0) {
			System.out.print("\n");
		}
		else {
		for(int i = 0 ; i < player_list[1].getOwned().size(); i ++) {
			if(i == player_list[1].getOwned().size() -1) {
				System.out.print(player_list[1].getOwned().get(i)+"\n");
			}
			else {
				System.out.print(player_list[1].getOwned().get(i)+",");
				}
			}
		}
		System.out.println(banker_list[0].getName()+"\t"+ banker_list[0].getMoney());
		if(player_list[0].getMoney() > player_list[1].getMoney()) {
			System.out.println("Winner "+player_list[0].getName());
		}
		else if(player_list[1].getMoney() > player_list[0].getMoney()) {
			System.out.println("Winner "+player_list[1].getName());
		}
		else if(player_list[1].getMoney() == player_list[0].getMoney()) {
			System.out.println("Winner "+player_list[0].getName()+",\t"+ player_list[1].getName());
		}
		
		System.out.println("-----------------------------------------------------------------------------------------------------------");
	}
}