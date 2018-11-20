package org.eod;

import javax.swing.plaf.basic.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
  
//public class Main extends JFrame
//{
//   public Main() {
//      addWindowListener(new WindowAdapter() {
//         public void windowClosing(WindowEvent we) {
//            System.exit(1);
//         }
//      });
//  
//      getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
//  
//      String[] elements = new String[] { "arthur", "brian", "chet", "danny", "dave", 
//                                         "don", "edmond", "eddy", "glen", "gregory", "john",
//                                         "ken", "malcolm", "pete", "stephanie", "yvonne" };
//  
//      JComboBox comboBox = new AutoCompleteComboBox(elements);
//      getContentPane().add(comboBox);
//   }
//       
//   public static void main(String []args) {
//      Main main = new Main();
//      main.setSize(300, 300);
//      main.setVisible(true);
//   }
//}
  
class AutoCompleteComboBox extends JComboBox
{
   public int caretPos=0;
   public JTextField inputField=null;
  
   public AutoCompleteComboBox(final Object elements[]) {
      super(elements);
      setEditor(new BasicComboBoxEditor());
      setEditable(true);
   }
  
   public void setSelectedIndex(int index) {
      super.setSelectedIndex(index);
  
      inputField.setText(getItemAt(index).toString());
      inputField.setSelectionEnd(caretPos + inputField.getText().length());
      inputField.moveCaretPosition(caretPos);
   }
  
   public void setEditor(ComboBoxEditor editor) {
      super.setEditor(editor);
      if (editor.getEditorComponent() instanceof JTextField) {
         inputField = (JTextField) editor.getEditorComponent();
  
         inputField.addKeyListener(new KeyAdapter() {
            public void keyReleased( KeyEvent ev ) {
               char key=ev.getKeyChar();
               if (! (Character.isLetterOrDigit(key) || Character.isSpaceChar(key) )) return;
  
               caretPos=inputField.getCaretPosition();
               String text="";
               try {
                  text=inputField.getText(0, caretPos).toUpperCase();
               }
               catch (javax.swing.text.BadLocationException e) {
                  e.printStackTrace();
               }
  
               for (int i=0; i<getItemCount(); i++) {
                  String element = (String) getItemAt(i);
                  if (element.startsWith(text)) {
                     setSelectedIndex(i);
                     return;
                  }
               }
            }
         });
      }
   }
}