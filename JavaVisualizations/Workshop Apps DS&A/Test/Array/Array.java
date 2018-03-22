// Array.java
// demonstrates unordered arrays
import java.awt.*;
import java.awt.event.*;
/////////////////////////////
class person
   {
   private int height;
   private Color color;

   public person(int h, Color c)
      { height = h; color = c; }

   public Color getColor()
      { return color; }

   public int getHeight()
      { return height; }
   }  // end class person
/////////////////////////
class personGroup
   {
   private final int appletWidth = 440;
   private final int appletHeight = 320;
   private final int topMargin = 80;     // top of cell array
   private final int leftMargin = 10;
   private final int textHeight = 13;
   private final int hF1 = 12;           // fudge factors to
   private final int hF2 = 6;            // position numbers
   private final int hF3 = 0;            // within cells
   private final int vF = 8;
   private final int nColumns = 5;       // columns in display
   private final int nCellsPerCol = 12;  // cells per column
   private final int columnWidth = 85;
   private final int cellWidth = 35;
   private final int cellHeight = 17;
   private final int digits3Width = 18;
   private final int vertSeparation = cellHeight + 2;
   private final int horizSeparation = cellWidth + 25;
   private final int noteBoxTop = topMargin - 25;
   private final int noteBoxHeight = 25;
   private final int noteBoxWidth = 325;
   private final int ASIZE = 60;  // maximum size of array
   private final int MAX_KEY = 999;

   private person personArray[];  // array for holding persons
   private int totalCells;        // current array size
   private int nPersons;          // persons inserted so far
   private person tempPers;       // for insert()
   private String note;           // text displayed on screen
   private int fillValue;         // value (from user) for fill
   private int insKey;            // key (from user) for insert
   private int findKey;           // key (from user) for find
   private int delKey;            // key (from user) for delete
   private int codePart;          // which part of sequence?
   private int codePart2;         // ditto, for fill()
   private int opMode;            // 1=New, 2=Fill, etc.
   private int curIn;             // current index (arrow)
   private int oldCurIn;
   private int lastDeletion;      // for delete with dups
   private int drawMode;          // 1=draw one cell,
                                  // 2=all cells, clear screen
                                  // 3=all cells, don't clear s
   private boolean dupsOK;     // duplicate keys allowed
   private boolean isOKChangeDups;  // OK to change dups status
   private boolean wasJustFound;  // person found in last cycle
   private boolean madeDeletion;  // used when dups OK
   private int nDeleted;          // number of cells deleted
// ------------------
   public personGroup(int cells)           // constructor
      {
      totalCells = cells;
      personArray = new person[totalCells];
      curIn = oldCurIn = 0;
      nPersons = 0;
      codePart = 1;
      codePart2 = 1;
      drawMode = 2;
      dupsOK = false;
      note = "Press any button";
      }  // end constructor
// ------------------
   public boolean getDupsStatus()
      { return dupsOK; }
// ------------------
   public boolean getChangeStatus()
      { return isOKChangeDups; }
// ------------------
   public void setDupsStatus(boolean dupsOKButton)
      {
      if(isOKChangeDups && dupsOKButton != dupsOK)
         dupsOK = dupsOKButton;
      if(!isOKChangeDups)
         note =
         "To change duplication status, create array with New";
      drawMode = 1;
      }
// ------------------
   // return a person of specified height and random RGB color
   public person makePerson(int height)
      {
      int red = 100 + (int)(java.lang.Math.random()*154);
      int green = 100 + (int)(java.lang.Math.random()*154);
      int blue = 100 + (int)(java.lang.Math.random()*154);
      Color newColor = new Color(red, green, blue);
      return new person(height, newColor);
      }
// ------------------
   // create a new empty array of specified size
   public void newArray(boolean isNumb, int userVal)
      {
      if(opMode != 1)
         {
         opMode = 1;
         codePart = 1;
         }
      switch(codePart)
         {
         case 1:
            note = "Enter size of array to create";
            drawMode = 1;
            codePart = 2;
            oldCurIn = curIn;
            curIn = 0;
            break;
         case 2:
            if(!isNumb || userVal < 0 || userVal > ASIZE)
               {
               note = "ERROR: use size between 0 and " + ASIZE;
               codePart = 1;      // inappropriate size
               }
            else
               {
               totalCells = userVal;
               note = "Will create empty array with "
                                       + totalCells + " cells";
               codePart = 3;
               }
            drawMode = 1;
            break;
         case 3:
            note = "Select Duplicates OK, or No Dups";
            isOKChangeDups = true;
            drawMode = 1;
            codePart = 5;
            break;
         // no case 4
         case 5:                             // create array
            personArray = new person[totalCells];
            for(int j=0; j<totalCells; j++)  // put null in
               personArray[j] = null;        // all cells
            nPersons = 0;
            note = "New array created; total items = " +
                                                   nPersons;
            isOKChangeDups = false;
            oldCurIn = curIn;
            curIn = 0;
            drawMode = 2;
            codePart = 6;
            break;
         case 6:
            note = "Press any button";
            drawMode = 1;
            codePart = 1;
            break;
         }  // end switch
      }  // end newArray()
// ------------------
   public void fill(boolean isNumb, int userVal)
      {
      if(opMode != 2)
         {
         opMode = 2;
         codePart2 = 1;
         }
      switch(codePart2)
         {
         case 1:
            note = "Enter number of items to fill in";
            drawMode = 1;
            codePart2 = 2;
            break;
         case 2:
            if(isNumb != true || userVal<0 || userVal>totalCells)
               {
               note = "ERROR: can't fill more than "
                                   + totalCells + " items";
               drawMode = 1;
               codePart2 = 1;
               }
            else
               {
               fillValue = userVal;
               note = "Will fill in " + fillValue + " items";
               drawMode = 1;
               codePart2 = 3;
               }
            break;
         case 3:
            nPersons = 0;
            doFill(fillValue);
            opMode = 2;
            note = "Fill completed; total items = " + nPersons;
            oldCurIn = curIn;
            curIn = 0;
            if( !dupsOK )
               checkDups();
            drawMode = 2;
            codePart2 = 4;
            break;
         case 4:
            note = "Press any button";
            drawMode = 1;
            codePart2 = 1;
            break;
         }  // end switch
      }  // end fill
// ------------------
   public void doFill(int totalPersons)
      {
      int j, randVal;
      for(j=0; j<totalCells; j++) // clear the array
         personArray[j] = null;
      oldCurIn = curIn;
      curIn = 0;
      codePart = 1;
      while(nPersons < totalPersons)
         {
         insert(true, 1000);      // codePart: 1
         randVal = (int)(java.lang.Math.random()*MAX_KEY);
         if(!dupsOK)
            {
            while( getDuplicate(randVal) != -1 )
               randVal = (int)(java.lang.Math.random()*MAX_KEY);
            }
         insert(true, randVal); // codePart: 2
         while(codePart != 1)
            insert( true, 1000 ); // 3, 3, 3, ..., 3, 5, 6, 1
         }
      drawMode = 2;
      }  // end doFill
// ------------------
   // returns index of first duplicate, or -1 if no dups
   public int getDuplicate(int value)
      {
      for(int j=0; j<totalCells; j++)
         if(personArray[j] != null &&
            personArray[j].getHeight() == value)
            return j;
      return -1;
      }
// ------------------
   public void checkDups()
      {
      for(int j=0; j<totalCells-1; j++)
         for(int k=j+1; k<totalCells; k++)
            if(personArray[j] != null &&
               personArray[k] != null &&
               personArray[j].getHeight() ==
               personArray[k].getHeight() )
               {
               note = "ERROR: " + j + " same as " + k;
               drawMode = 2;
               return;
               }
      }
// ------------------
   // insert a person
   public void insert(boolean isNumb, int userVal)
      {
      if(opMode != 3)
         {
         opMode = 3;
         codePart = 1;
         }
      switch(codePart)
         {
         case 1:
            oldCurIn = curIn;
            curIn = 0;
            note = "Enter key of item to insert";
            drawMode = 1;
            codePart = 2;
            break;
         case 2:
            if(!isNumb || userVal < 0 || userVal > MAX_KEY)
               {
               note="CAN'T INSERT: need key between 0 and " +
                                                      MAX_KEY;
               codePart = 1;
               }
            else if(nPersons >= totalCells)
               {
               note = "CAN'T INSERT: array is full";
               codePart = 6;
               }
            else                  // can insert
               {
               insKey = userVal;
                                  // make person to insert
               tempPers = makePerson(insKey);
               note = "Will insert item with key " + insKey;
               codePart = 4;
               }
            drawMode = 1;
            break;
         // no case 3
         case 4:
            oldCurIn = curIn;
            curIn = nPersons;
            personArray[curIn] = tempPers;
            nPersons++;
            note="Inserted item with key " + insKey +
                 " at index " + curIn;
            drawMode = 1;
            codePart = 5;
            break;
         case 5:
            note = "Insertion completed; total items = "
                                               + nPersons;
            if( !dupsOK )
               checkDups();  // (changes msg if duplicate)
            drawMode = 2;
            codePart = 6;
            break;
         case 6:
            oldCurIn = curIn;
            curIn = 0;
            note = "Press any button";
            drawMode = 1;
            codePart = 1;
            break;
         }  // end switch
      }  // end insert()
// ------------------
   public void find(boolean isNumb, int userVal)
      {
      if(opMode != 4)
         {
         opMode = 4;
         codePart = 1;
         }
      switch(codePart)
         {
         case 1:
            oldCurIn = curIn;
            curIn = 0;
            note = "Enter key of item to find";
            codePart = 2;
            break;
         case 2:
            if(!isNumb || userVal < 0 || userVal > MAX_KEY)
               {
               note="ERROR: use key between 0 and " + MAX_KEY;
               codePart = 1;      // inappropriate person number
               }
            else
               {
               findKey = userVal;
               note = "Looking for item with key " + findKey;
               codePart = 3;
               }
            break;
         case 3:
            if(curIn >= nPersons)
               {                            // no such person
               note="Can't locate item with key " + findKey;
               codePart = 6;
               }
            else if(personArray[curIn].getHeight() == findKey)
               {                            // match
               note="Have found item with key " + findKey;
               wasJustFound = true;
               if(dupsOK)
                  codePart = 4;
               else
                  codePart = 6;
               }
            else                            // check next person
               {
               oldCurIn = curIn;
               curIn++;
               note = "Checking next cell; index = " + curIn;
               codePart = 3;
               }
            break;
         case 4:  // found one match, look for others
            if(wasJustFound)                // keep arrow at
               {                            // match if
               oldCurIn = curIn;            // appropriate
               curIn++;
               }
            if(curIn >= nPersons)
               {                            // cell not occupied
               note="No additional items with key " + findKey;
               codePart = 6;
               }
            else if(personArray[curIn].getHeight() == findKey)
               {                            // match
               note="Have found additional item with key "
                    + findKey + " at index " + curIn;
               wasJustFound = true;
               codePart = 4;
               }
            else                            // check next person
               {
               if(!wasJustFound)
                  {
                  oldCurIn = curIn;
                  curIn++;
                  }
               wasJustFound = false;
               note = "Checking for additional matches; index = "
                      + curIn;
               codePart = 4;
               }  // end else check next person
            break;
         // no case 5
         case 6:
            oldCurIn = curIn;
            curIn = 0;
            note = "Press any button";
            codePart = 1;
            break;
         }  // end switch
      drawMode = 1;             // never any changes during find
      }  // end find()
// ------------------
   public void delete(boolean isNumb, int userVal)
      {
      if(opMode != 5)
         {
         opMode = 5;
         codePart = 1;
         }
      switch(codePart)
         {
         case 1:
            oldCurIn = curIn;
            curIn = 0;
            lastDeletion = -1;
            nDeleted = 0;        // number of duplicates deleted
            note = "Enter key of item to delete";
            codePart = 2;
            break;
         case 2:
            if(!isNumb || userVal < 0 || userVal > MAX_KEY)
               {
               note="ERROR: use key between 0 and " + MAX_KEY;
               codePart = 1;      // inappropriate person number
               }
            else
               {
               delKey = userVal;
               note = "Looking for item with key " + delKey;
               codePart = 3;
               }
            break;
         case 3:                            // check for match
            if(curIn >= nPersons)
               {
               if(lastDeletion == -1)       // no deletions
                  {
                  note="No item with key " + delKey + " found";
                  codePart = 5;
                  }
               else                         // were deletion(s)
                  {
                  note="No additional items with key "
                       + delKey + " found";
                  codePart = 6;
                  }
               }
            else if(personArray[curIn].getHeight() == delKey)
               {                            // match
               personArray[curIn] = null;
               note="Have found and deleted item with key " +
                    delKey;
               lastDeletion = curIn;
               if(dupsOK)
                  {
                  nDeleted = 1;
                  codePart = 10;
                  }
               else
                  codePart = 4;
               }
            else                            // check next person
               {
               oldCurIn = curIn;
               curIn++;
               note = "Checking index = " + curIn
                      + " for item";
               codePart = 3;
               }  // end else check next person
            break;
         case 4:
            if(curIn < nPersons-1)          // shift cells down
               {
               oldCurIn = curIn;
               curIn++;
               personArray[curIn-1] = personArray[curIn];
               personArray[curIn] = null;
               note = "Shifted item from " + curIn +
                      " to " + (curIn-1);
               codePart = 4;
               }  // end if(shift cells down)
            else                           // done shifting
               {
               nPersons--;
               note = "Shifting completed. Total items = " +
                      nPersons;
               oldCurIn = curIn;
               curIn = nPersons-1;
               if(dupsOK)               // if dups OK,
                  {
                  curIn = lastDeletion;    // check for more
                  codePart = 3;
                  }
               else                        // not OK,
                  codePart = 6;            // we're done
               }
            break;
         case 5:
            note = "Deletion not completed";
            codePart = 6;
            break;
         case 6:
            oldCurIn = curIn;
            curIn = 0;
            note = "Press any button";
            codePart = 1;
            break;
         // no cases 7, 8, 9
         case 10:
            oldCurIn = curIn;
            curIn += nDeleted;    // cursor on item to be shifted
            note = "Will shift item " + nDeleted + " spaces";
            codePart = 11;
            break;
         case 11:                          // multiple deletions
            if(curIn < nPersons)           // shift cell down
               {
               personArray[curIn-nDeleted] = personArray[curIn];
               personArray[curIn] = null;
               note = "Shifted item from " + curIn +
                      " to " + (curIn-nDeleted);
               oldCurIn = curIn;
               curIn -= nDeleted;
               codePart = 12;              // go check for match
               }
            else
               {
               nPersons -= nDeleted;
               note = "Shifts complete; no more items to delete";
               codePart = 6;               // done
               }
            break;
         case 12:                          // check for match
            if(personArray[curIn].getHeight() == delKey)
               {                           // match
               nDeleted++;
               personArray[curIn] = null;
               note=
               "Have deleted additional item with key " + delKey;
               lastDeletion = curIn;
               oldCurIn = curIn;
               curIn += nDeleted;
               }
            else
               {
               note = "Item at " + curIn + " is not a duplicate";
               oldCurIn = curIn;
               curIn += nDeleted+1;
               }
            codePart = 11;
            break;
         }  // end switch
      drawMode = 1;      // only two cells changed during delete
      }  // end delete()
// ------------------
 public void drawPerson(Graphics g, int persDex)
   {
   if(persDex > totalCells-1) // cell not in the array
      return;
   int x, y;
   int hF, height;

   x = leftMargin + columnWidth * (persDex / nCellsPerCol);
   y = topMargin + 9 + cellHeight * (persDex % nCellsPerCol);

   if(persDex<10)       hF = hF1;  // fudge factors for digits
   else if(persDex<100) hF = hF2;
   else                 hF = hF3;

   if(drawMode==2)
      {
      g.setColor(Color.black);     // draw array index
      g.drawString(""+persDex, x + hF, y + cellHeight - vF);
      }
   g.setColor(Color.black);        // draw rectangle
   g.drawRect(x+digits3Width+5, y-5, cellWidth, cellHeight);

   if(personArray[persDex]==null)  // if cell not occupied,
      {
      g.setColor(Color.lightGray); // fill rectangle w/ backgnd
      g.fill3DRect(x+digits3Width+6, y-4, cellWidth-1,
                                          cellHeight-1, true);
      }
   else                            // cell is occupied
      {                            // get height and color
      height = personArray[persDex].getHeight();
      g.setColor( personArray[persDex].getColor() );
                                   // fill rectangle with color
      g.fill3DRect(x+digits3Width+6, y-4, cellWidth-1,
                                          cellHeight-1, true);
      if(height<10)       hF = hF1; // fudge factors for digits
      else if(height<100) hF = hF2;
      else                hF = hF3;
      g.setColor(Color.black);     // draw height number
      g.drawString(""+height, x + digits3Width + hF +15,
                              y + cellHeight - vF);
      }

   if( persDex==curIn )            // draw arrow
      g.setColor(Color.red);         // for curIn, red arrow
   else                              // for all other persons,
      g.setColor(Color.lightGray);   // gray arrow
   int xTip = x + digits3Width + 8 + cellWidth;
   int yTip = y + cellHeight / 2 - 4;
   g.drawLine(xTip, yTip,   xTip+20, yTip);    // shaft
   g.drawLine(xTip, yTip+1, xTip+20, yTip+1);
   g.drawLine(xTip, yTip,   xTip+5,  yTip-5);  // top feather
   g.drawLine(xTip, yTip+1, xTip+5,  yTip-4);
   g.drawLine(xTip, yTip,   xTip+5,  yTip+5);  // bottom feather
   g.drawLine(xTip, yTip+1, xTip+5,  yTip+6);
   }
// ------------------
   public void draw(Graphics g)      // draw array
      {
      int j, x, y;

      if(drawMode==1)                   // draw only one person
         {                              //    (plus old one)
         g.setColor(Color.lightGray);   // clear text area
         g.fillRect(leftMargin, noteBoxTop, noteBoxWidth,
                    noteBoxHeight);
         g.setColor(Color.black);       // draw 'note'
         g.drawString(note, leftMargin+6,
                      noteBoxTop+textHeight+6);

         drawPerson(g, oldCurIn);       // (to erase arrow)
         drawPerson(g, curIn);          // draw current person
         drawMode = 2;
         }
      else if(drawMode==2)              // draw all persons
         {
         g.setColor(Color.lightGray);   // clear entire screen
         g.fillRect(0, 0, appletWidth, appletHeight);

         for(j=0; j<totalCells; j++)    // draw all cells
            drawPerson(g, j);
         g.setColor(Color.black);       // draw text ('note')
         g.drawString(note, leftMargin+6,
                      noteBoxTop+textHeight+6);
         }  // end if drawMode is 2
      else                              // drawMode is 3
         {
         g.setColor(Color.lightGray);   // clear text area
         g.fillRect(leftMargin, noteBoxTop, noteBoxWidth,
                    noteBoxHeight);
         for(j=0; j<totalCells; j++)    // draw all cells
            drawPerson(g, j);
         g.setColor(Color.black);       // draw text ('note')
         g.drawString(note, leftMargin+6,
                      noteBoxTop+textHeight+6);
         }  // end else drawMode is 3
      drawMode = 2;
      }  // end draw()
// ------------------
   }  // end class personGroup
////////////////////////////
public class Array extends java.applet.Applet
                     implements ActionListener, ItemListener,
                                Runnable
   {
   private volatile Thread runner;
   private personGroup thePersonGroup;
   private int GPNumber = -1;      // general-purpose number
   private boolean isNumber = false;  // is GPNumber valid
   private TextField tf = new TextField("", 4);
   private Checkbox dups, nodups;
   private Button newButton, fillButton, insButton,
                  findButton, delButton;
// ------------------
   public void init()
      {
      setLayout( new FlowLayout() );
      Panel p1 = new Panel();
      add(p1);
      p1.setLayout( new FlowLayout() );

      Panel p2 = new Panel();
      p1.add(p2);
      p2.setLayout( new FlowLayout(FlowLayout.LEFT) );

      newButton = new Button("New");     // New button
      p2.add( newButton );
      newButton.addActionListener(this);

      fillButton = new Button("Fill");   // Fill button
      p2.add( fillButton );
      fillButton.addActionListener(this);

      insButton = new Button("Ins");     // Ins button
      p2.add( insButton );
      insButton.addActionListener(this);

      findButton = new Button("Find");   // Find button
      p2.add( findButton );
      findButton.addActionListener(this);

      delButton = new Button("Del");     // Del button
      p2.add( delButton );
      delButton.addActionListener(this);

      Panel p3 = new Panel();     // checkboxes for probe
      p1.add(p3);
      p3.setLayout( new GridLayout(2, 1) );
      CheckboxGroup theGroup = new CheckboxGroup();
      dups =   new Checkbox("Dups OK", false, theGroup);
      nodups = new Checkbox("No dups", true, theGroup);
      p3.add(dups);
      dups.addItemListener(this);
      p3.add(nodups);
      nodups.addItemListener(this);

      Panel p4 = new Panel();     // text field
      p1.add(p4);
      p4.setLayout( new  FlowLayout(FlowLayout.RIGHT) );
      p4.add( new Label("Number: ") );
      p4.add(tf);
                                  // start with 20 cells
      thePersonGroup = new personGroup(20);
      thePersonGroup.doFill(10);  // of which 10 are filled
      repaint();
      }  // end init()
// ------------------
   public void paint(Graphics gg)
      { thePersonGroup.draw(gg); }
// ------------------
   public void update(Graphics gg)
      { paint(gg); }
// ------------------
   public void actionPerformed(ActionEvent event)  // buttons
      {
      isNumber = true;
      String s = tf.getText();          // get the number
                                        // convert string
      try{ GPNumber = Integer.parseInt( s ); }  // to number
      catch(NumberFormatException e)
         {                              // not a number
         GPNumber = 0;
         isNumber = false;
         }
      if(event.getSource() == newButton)        // New button
         thePersonGroup.newArray(isNumber, GPNumber);
      else if(event.getSource() == fillButton)  // Fill button
         thePersonGroup.fill(isNumber, GPNumber);
      else if(event.getSource() == insButton)   // Ins button
         thePersonGroup.insert(isNumber, GPNumber);
      else if(event.getSource() == findButton)  // Find button
         thePersonGroup.find(isNumber, GPNumber);
      else if(event.getSource() == delButton)   // Del button
         thePersonGroup.delete(isNumber, GPNumber);

      repaint();                                // all buttons
      try{ Thread.sleep(10); }
      catch(InterruptedException e)
         {  }
      }  // end actionPerformed
// ------------------
   public void itemStateChanged(ItemEvent event)  // checkboxes
      {                                      // (radio buttons)
      boolean isDups = event.getSource() == dups;
      boolean wasDups = thePersonGroup.getDupsStatus();
      boolean canChange = thePersonGroup.getChangeStatus();
      thePersonGroup.setDupsStatus(isDups);

      if( (isDups && canChange && !wasDups) ||
          (!isDups && !canChange && wasDups) )
         {
         dups.setState(true);
         nodups.setState(false);
         }
      if( (!isDups && canChange && wasDups) ||
          (isDups && !canChange && !wasDups) )
         {
         dups.setState(false);
         nodups.setState(true);
         }
      }  // end itemStateChanged()
// ------------------
   public void start()
      {
      runner = new Thread(this);
      runner.start();
      }
// ------------------
   public void stop()
      {
      runner = null;
      }
// ------------------
   public void run()
      {
      Thread thisThread = Thread.currentThread();
      while(runner == thisThread)
         {
         }
      }  // end run()
// ------------------
   }  // end class Array
//////////////////////////

