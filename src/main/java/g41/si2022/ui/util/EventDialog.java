package g41.si2022.ui.util;

import java.awt.GridBagConstraints;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import g41.si2022.dto.SesionDTO;
import g41.si2022.ui.components.BetterDatePicker;

public class EventDialog extends javax.swing.JDialog {

    private boolean pressedOk;
    private JTextField inputLocation;
    private BetterDatePicker inputDate;
    private TimePicker inputStartHour;
    private TimePicker inputEndHour;
    private BetterDatePicker inputRepeatUntil;
    private JCheckBox inputRepeat;
    private JButton okButton;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<SesionDTO> sesiones;

    private EventDialog(java.awt.Frame parent, boolean model) {
        super(parent, "Añadir nuevo sesion", model);
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

    public EventDialog(LocalDate start, LocalDate end, SesionDTO last) {
        this(start, end);
        inputLocation.setText(last.getLoc());
        inputDate.setDate(LocalDate.parse(last.getFecha()).plusDays(1));
        inputStartHour.setTime(LocalTime.parse(last.getHoraIni()));
        inputEndHour.setTime(LocalTime.parse(last.getHoraFin()));
    }

    public boolean showDialog() {
        pressedOk = false;
        this.setVisible(true);
        return pressedOk;
    }

    public List<SesionDTO> getSesiones() {
        return this.sesiones;
    }

    private void initComponents() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 300);

        TimePickerSettings tps = new TimePickerSettings();
        tps.use24HourClockFormat();
        tps.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.ThirtyMinutes, null, null);

        this.setLayout(new java.awt.GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10 ,10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(JLabelFactory.getLabel("Localización (aula):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(inputLocation = new JTextField(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(JLabelFactory.getLabel("Fecha:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(inputDate = new BetterDatePicker(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(JLabelFactory.getLabel("Hora de inicio:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputStartHour = new TimePicker(tps);
        this.add(inputStartHour, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(JLabelFactory.getLabel("Hora de fin:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputEndHour = new TimePicker(tps);
        this.add(inputEndHour, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(JLabelFactory.getLabel("Repetir hasta:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JPanel panelRepetir = new JPanel();
        panelRepetir.setLayout(new java.awt.BorderLayout());
        panelRepetir.add(inputRepeat = new JCheckBox(), java.awt.BorderLayout.WEST);
        inputRepeat.addActionListener((e) -> {inputRepeatUntil.setEnabled(inputRepeat.isSelected());});
        panelRepetir.add(inputRepeatUntil = new BetterDatePicker(), java.awt.BorderLayout.EAST);
        inputRepeatUntil.setEnabled(false);
        this.add(panelRepetir, gbc);

        okButton = new JButton("Aceptar");
        okButton.addActionListener((e) -> {
            pressedOk = true;
            generateSesiones();
            this.setVisible(false);
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
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

        for(JComponent c : new JComponent[] {inputLocation, inputDate, inputStartHour, inputEndHour, inputRepeat, inputRepeatUntil}) {
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
            } else if (c instanceof JCheckBox) {
                ((JCheckBox) c).addActionListener((e) -> {
                    checkValidity();
                });
            }
        }

        okButton.setEnabled(false);
    }

    private void generateSesiones() {
        sesiones = new java.util.LinkedList<>();
        sesiones.add(new SesionDTO(inputLocation.getText(), inputDate.getDate().toString(), inputStartHour.getTime().toString(), inputEndHour.getTime().toString()));
        if(inputRepeat.isSelected()) {
            LocalDate date = inputDate.getDate();
            date = date.plusDays(7);
            while(date.isBefore(inputRepeatUntil.getDate()) || date.equals(inputRepeatUntil.getDate())) {
                sesiones.add(new SesionDTO(inputLocation.getText(), date.toString(), inputStartHour.getTime().toString(), inputEndHour.getTime().toString()));
                date = date.plusDays(7);
            }
        }
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

        if(inputRepeat.isSelected()) {
            if(inputRepeatUntil.getDate() == null) {
                okButton.setEnabled(false);
                return;
            }

            if(inputRepeatUntil.getDate().isBefore(inputDate.getDate())) {
                okButton.setEnabled(false);
                Dialog.showError("Fecha de repetición fuera de rango.");
                inputRepeatUntil.setDate(null);
                return;
            }

            if(inputRepeatUntil.getDate().isAfter(endDate)) {
                inputRepeatUntil.setDate(inputDate.getDate());
            }
        }

        okButton.setEnabled(true);
    }

}
