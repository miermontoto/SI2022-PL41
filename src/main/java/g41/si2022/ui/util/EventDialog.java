package g41.si2022.ui.util;

import java.awt.GridBagConstraints;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import g41.si2022.ui.components.BetterDatePicker;

public class EventDialog extends javax.swing.JDialog {

    private boolean pressedOk;
    private JTextField inputLocation;
    private BetterDatePicker inputDate;
    private TimePicker inputStartHour;
    private TimePicker inputEndHour;
    private JButton okButton;

    private LocalDate startDate;
    private LocalDate endDate;

    private EventDialog(java.awt.Frame parent, boolean model) {
        super(parent, "Añadir nuevo evento", model);
        FlatLightLaf.setup();
        initComponents();
        this.setVisible(false);
        startDate = LocalDate.parse("1970-01-02");
        endDate = LocalDate.parse("2038-01-02");
    }

    public static void main(String[] args) {
        EventDialog dialog = new EventDialog(null, true);
        dialog.showDialog();
        System.exit(0);
    }

    public EventDialog(LocalDate startDate, LocalDate endDate) {
        this(null, true);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean showDialog() {
        pressedOk = false;
        this.setVisible(true);
        return pressedOk;
    }

    public String getLoc() {
        return inputLocation.getText();
    }

    public String getDate() {
        return inputDate.getDate().toString();
    }

    public String getStart() {
        return inputStartHour.getText();
    }

    public String getEnd() {
        return inputEndHour.getText();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 250);

        TimePickerSettings tps = new TimePickerSettings();
        tps.use24HourClockFormat();
        tps.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.FifteenMinutes, LocalTime.parse("09:00"), LocalTime.parse("21:00"));

        this.setLayout(new java.awt.GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10 ,10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(new JLabel("Localización (aula):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(inputLocation = new JTextField(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(new JLabel("Fecha:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(inputDate = new BetterDatePicker(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(new JLabel("Hora de inicio:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputStartHour = new TimePicker(tps);
        this.add(inputStartHour, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(new JLabel("Hora de fin:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputEndHour = new TimePicker(tps);
        this.add(inputEndHour, gbc);

        okButton = new JButton("Aceptar");
        okButton.addActionListener((e) -> {
            pressedOk = true;
            this.setVisible(false);
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(okButton, gbc);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener((e) -> {
            pressedOk = false;
            this.setVisible(false);
        });
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(cancelButton, gbc);

        for(JComponent c : new JComponent[] {inputLocation, inputDate, inputStartHour, inputEndHour}) {
            if (c instanceof JTextField) {
                    c.addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        checkValidity();
                    }
                });
            } else if (c instanceof BetterDatePicker) {
                ((BetterDatePicker) c).addDateChangeListener((e) -> {
                    checkValidity();
                });
            } else if (c instanceof TimePicker) {
                ((TimePicker) c).addTimeChangeListener((e) -> {
                    checkValidity();
                });
            }
        }

        okButton.setEnabled(false);
    }

    private void checkValidity() {
        if(inputStartHour.getTime() != null && inputEndHour.getTime() != null) {
            if (inputEndHour.getTime().isBefore(inputStartHour.getTime()) ||
                    inputStartHour.getTime().equals(inputEndHour.getTime())) {
                okButton.setEnabled(false);
                Dialog.showError("Rango horario inválido");
                inputEndHour.setTime(null);
                return;
            }
        }

        if(inputDate.getDate() != null) {
            if (inputDate.getDate().isBefore(startDate)) {
                okButton.setEnabled(false);
                Dialog.showError("Fecha de inicio fuera de rango.");
                inputDate.setDate(null);
                return;
            }

            if(inputDate.getDate().isAfter(endDate) || inputDate.getDate().isBefore(startDate)) {
                okButton.setEnabled(false);
                Dialog.showError("Fecha fuera de rango.");
                inputDate.setDate(null);
                return;
            }
        }

        if(inputLocation.getText().isEmpty() || inputDate.getDate() == null || inputStartHour.getText().isEmpty() || inputEndHour.getText().isEmpty()) {
            okButton.setEnabled(false);
            return;
        }

        okButton.setEnabled(true);
    }

}
