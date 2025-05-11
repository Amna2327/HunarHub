**DONE**
- Introduced Logic for BuyerMenu
- Created bank accounts for both Maker and Buyer and added its logic too
- Both Buyer and Maker have the SAME attributes for now, plus an integer number to differentiate between them (1 = Maker, 0 = Buyer)
- Added some file handling (read preexisting users from Userfile and store them to ArrayList at the start of the program, write user to file when account is created)

**TO-DO LIST** 
- Need to work on BuyerMenu cases involving products, users and buying/transactions
  
**PROBLEMS ENCOUNTERED**
- link individual bank accounts to a central Bank (?)
- using direct paths in the same directory didn't seem to work out in my case (no data written to file but no exceptions thrown either). Going with global paths for now
