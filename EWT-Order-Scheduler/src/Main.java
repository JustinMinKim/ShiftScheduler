import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * User View.
 */
public class Main {

    /**
     * Number of drinks.
     */
    static int numDrinks;

    /**
     * Delivery time.
     */
    static LocalTime deliveryTime;

    /**
     * Main func.
     * @param args: arguments.
     * @throws UnsupportedLookAndFeelException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        init();
    }

    /**
     * Helper to calculate the shifts for the order.
     */
    public static void calculateShifts() {
        Scheduler.scheduleSealingAndDelivery(numDrinks, deliveryTime);
        Scheduler.scheduleCooking(numDrinks);
    }

    /**
     * Initialize input view for user (first frame) and calculate shifts.
     */
    public static void init() {
        JFrame frame = new JFrame("East-West Tea Order Scheduler");
        frame.setSize(500,200);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel panel0 = new JPanel();
        panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
        frame.add(panel0);

        JPanel panel1 = new JPanel();
        panel1.setBackground(new Color(0xFFB6C7FF, true));
        panel0.add(panel1);
        JLabel numDrinksLabel = new JLabel("Number of Drinks: ", Label.LEFT);
        JTextField numDrinksTF = new JTextField(7);
        panel1.add(numDrinksLabel);
        panel1.add(numDrinksTF);


        final int[] hourText = {0};
        final int[] minuteText = {0};

        JPanel panel2 = new JPanel();
        panel2.setBackground(new Color(0xFFB6C7FF, true));
        panel0.add(panel2);
        JLabel timeDeliveryLabel = new JLabel("Time to be Delivered: ", Label.LEFT);
        JTextField hour = new JTextField("00", 3);
        hour.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
               if (hour.getText().trim().equals("00")) {
                   hour.setText("");
               } else {
                   //do nothing
               }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        JLabel semicolon = new JLabel(":");
        JTextField minute = new JTextField("00", 3);
        minute.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (minute.getText().trim().equals("00")) {
                    minute.setText("");
                } else {
                    //do nothing
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (minute.getText().trim().equals("")) {
                    minute.setText("00");
                } else {
                    //do nothing
                }
            }
        });
        String amOrPmList[] = {"AM", "PM"};
        JList amOrPm = new JList(amOrPmList);
        amOrPm.setSelectedIndex(0);
        panel2.add(timeDeliveryLabel);
        panel2.add(hour);
        panel2.add(semicolon);
        panel2.add(minute);
        panel2.add(amOrPm);

        JPanel panel3 = new JPanel();
        panel3.setBackground(new Color(0xFFB6C7FF, true));
        panel0.add(panel3);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get numDrinks.
                try {
                    numDrinks = Integer.parseInt(numDrinksTF.getText());
                } catch (Exception e1) {
                    System.err.println("Error: Please enter an integer for the number of drinks.");
                }

                // Get hour.
                try {
                    if (Integer.parseInt(hour.getText()) == 12) {
                        hourText[0] = 0;
                    } else {
                        hourText[0] = Integer.parseInt(hour.getText());
                    }
                } catch (Exception e1) {
                    System.err.println("ERROR: Please insert a valid integer into the hour field.");
                }
                System.out.println(hourText[0]);

                // Get minute.
                try {
                    minuteText[0] = Integer.parseInt(minute.getText());
                } catch (Exception e1) {
                    System.err.println("ERROR: Please insert a valid integer into the minute field.");
                }

                // Get am or pm.
                if (amOrPm.getSelectedIndex() == 0) {
                    deliveryTime = LocalTime.of(hourText[0], minuteText[0], 0 );
                } else {
                    deliveryTime = LocalTime.of(hourText[0] + 12, minuteText[0], 0);
                }

                // Calculate shifts.
                calculateShifts();

                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

                // Then, display output.
                outputGUI();
            }
        });
        panel3.add(submitButton);
        frame.setVisible(true);
    }

    /**
     * Initialize the output GUI (second frame).
     */
    public static void outputGUI() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String[] colNames = {"Shift Name", "Number of Workers", "Start Time", "Leave By Time", "End Time"};
        Object[][] data = {{"Cooking", Scheduler.cooking.numWorkers, Scheduler.cooking.timeStart.format(formatter), "N/A", Scheduler.cooking.timeEnd.format(formatter)},
                {"Sealing/Delivery", Scheduler.sealDel.numWorkers, Scheduler.sealDel.timeStart.format(formatter), Scheduler.leaveByTime.format(formatter), Scheduler.sealDel.timeEnd.format(formatter)}};
        JTable shiftTable = new JTable(data, colNames);
        shiftTable.getTableHeader().setReorderingAllowed(false);
        shiftTable.setDefaultEditor(Object.class, null);
        shiftTable.setGridColor(new Color(130, 137, 208));
        shiftTable.setAlignmentX(Component.CENTER_ALIGNMENT);
        shiftTable.getTableHeader().setBackground(new Color(0xFFB6C7FF, true));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        shiftTable.setDefaultRenderer(Object.class, centerRenderer);

        JFrame outputFrame = new JFrame("East-West Tea Order Scheduler Results");
        outputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        outputFrame.setSize(500,200);
        outputFrame.setResizable(false);
        outputFrame.setLocationRelativeTo(null);

        JPanel outputPanel = new JPanel();
        outputFrame.add(outputPanel);
        JScrollPane outputScroll = new JScrollPane(shiftTable);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        shiftTable.setRowHeight(60);
        shiftTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        shiftTable.getColumnModel().getColumn(1).setPreferredWidth(55);
        shiftTable.getColumnModel().getColumn(2).setPreferredWidth(10);
        shiftTable.getColumnModel().getColumn(3).setPreferredWidth(30);
        shiftTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        outputFrame.setVisible(true);
    }
}
