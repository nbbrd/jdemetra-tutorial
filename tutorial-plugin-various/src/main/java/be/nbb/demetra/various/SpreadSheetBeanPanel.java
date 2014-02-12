/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.various;

import ec.nbdemetra.ui.calendars.CustomDialogDescriptor;
import ec.nbdemetra.ui.completion.JAutoCompletionService;
import static ec.nbdemetra.ui.completion.JAutoCompletionService.DATE_PATTERN_PATH;
import static ec.nbdemetra.ui.completion.JAutoCompletionService.LOCALE_PATH;
import ec.nbdemetra.ui.properties.FileLoaderFileFilter;
import ec.tss.tsproviders.IFileLoader;
import ec.tss.tsproviders.TsProviders;
import ec.tss.tsproviders.spreadsheet.SpreadSheetBean;
import ec.tss.tsproviders.spreadsheet.SpreadSheetProvider;
import ec.tss.tsproviders.utils.DataFormat;
import ec.tss.tsproviders.utils.IConstraint;
import ec.util.completion.ext.DesktopFileAutoCompletionSource;
import ec.util.completion.swing.FileListCellRenderer;
import ec.util.completion.swing.JAutoCompletion;
import ec.util.desktop.Desktop;
import ec.util.desktop.DesktopManager;
import ec.util.various.swing.TextPrompt;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDescriptor;

/**
 *
 * @author charphi
 */
public class SpreadSheetBeanPanel extends javax.swing.JPanel {

    public static final String FILE_PROPERTY = "file";
    public static final String DATA_FORMAT_PROPERTY = "dataFormat";
    private final IFileLoader loader;
    private final JFileChooser fileChooser;
    private final Previewer previewer;

    /**
     * Creates new form SpreadsheetBeanPanel
     */
    public SpreadSheetBeanPanel() {
        initComponents();

        this.loader = TsProviders.lookup(IFileLoader.class, SpreadSheetProvider.SOURCE).get();
        this.fileChooser = new JFileChooser();
        this.previewer = new Previewer();

        fileChooser.setFileFilter(new FileLoaderFileFilter(loader));

        File[] paths = loader.getPaths();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        JAutoCompletion autoCompletion = new JAutoCompletion(filePathTextField);
        autoCompletion.setSource(new DesktopFileAutoCompletionSource(new FileLoaderFileFilter(loader), paths));
        autoCompletion.getList().setCellRenderer(new FileListCellRenderer(null, executor, paths));

        filePathTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                firePropertyChange(FILE_PROPERTY, null, getFile());
            }
        });

        TextPrompt prompt = new TextPrompt("file path", filePathTextField);
        prompt.setEnabled(false);
        if (DesktopManager.get().isSupported(Desktop.Action.SEARCH)) {
            prompt.setText(prompt.getText() + " or desktop search");
        }

        JAutoCompletionService.forPathBind(LOCALE_PATH, localeTextField);
        new TextPrompt("locale", localeTextField).setEnabled(false);
        localeTextField.getDocument().addDocumentListener(previewer);

        JAutoCompletionService.forPathBind(DATE_PATTERN_PATH, datePatternTextField);
        new TextPrompt("date pattern", datePatternTextField).setEnabled(false);
        datePatternTextField.getDocument().addDocumentListener(previewer);

        new TextPrompt("number pattern", numberPatternTextField).setEnabled(false);
        numberPatternTextField.getDocument().addDocumentListener(previewer);
    }

    private final class Previewer implements DocumentListener {

        private final Date dateSample = new Date();
        private final Number numberSample = 1234.5;

        @Override
        public void removeUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            DataFormat dataFormat = getDataFormat();
            datePatternPreview.setText(dataFormat.dateFormatter().tryFormatAsString(dateSample).or("\u203C "));
            numberPatternPreview.setText(dataFormat.numberFormatter().tryFormatAsString(numberSample).or("\u203C "));
        }
    }

    public void loadBean(SpreadSheetBean bean) {
        setFile(bean.getFile());
        setDataFormat(bean.getDataFormat());
    }

    public void storeBean(SpreadSheetBean bean) {
        bean.setFile(getFile());
        bean.setDataFormat(getDataFormat());
    }

    public File getFile() {
        return new File(filePathTextField.getText());
    }

    public void setFile(File file) {
        filePathTextField.setText(file.getPath());
    }

    public DataFormat getDataFormat() {
        return DataFormat.create(localeTextField.getText(), datePatternTextField.getText(), numberPatternTextField.getText());
    }

    public void setDataFormat(DataFormat df) {
        localeTextField.setText(df.getLocaleString());
        datePatternTextField.setText(df.getDatePattern());
        numberPatternTextField.setText(df.getNumberPattern());
    }

    public FileFilter getFileFilter() {
        return loader;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        filePathTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        browseButton = new javax.swing.JButton();
        localeTextField = new javax.swing.JTextField();
        datePatternTextField = new javax.swing.JTextField();
        datePatternPreview = new javax.swing.JLabel();
        numberPatternTextField = new javax.swing.JTextField();
        numberPatternPreview = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.jLabel1.text")); // NOI18N

        filePathTextField.setText(org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.filePathTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.jLabel2.text")); // NOI18N

        browseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder-open-table.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        localeTextField.setText(org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.localeTextField.text")); // NOI18N

        datePatternTextField.setText(org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.datePatternTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(datePatternPreview, org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.datePatternPreview.text")); // NOI18N

        numberPatternTextField.setText(org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.numberPatternTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(numberPatternPreview, org.openide.util.NbBundle.getMessage(SpreadSheetBeanPanel.class, "SpreadSheetBeanPanel.numberPatternPreview.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filePathTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(localeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(datePatternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(datePatternPreview))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(numberPatternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numberPatternPreview)))
                        .addGap(0, 211, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(localeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datePatternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePatternPreview))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberPatternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numberPatternPreview))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathTextField.setText(fileChooser.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel datePatternPreview;
    private javax.swing.JTextField datePatternTextField;
    private javax.swing.JTextField filePathTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField localeTextField;
    private javax.swing.JLabel numberPatternPreview;
    private javax.swing.JTextField numberPatternTextField;
    // End of variables declaration//GEN-END:variables

    public DialogDescriptor createDialogDescriptor(String title) {
        return new SpreadSheetBeanDialogDescriptor(this, title);
    }

    static class SpreadSheetBeanDialogDescriptor extends CustomDialogDescriptor<SpreadSheetBeanPanel> {

        SpreadSheetBeanDialogDescriptor(SpreadSheetBeanPanel p, String title) {
            super(p, title, p);
            validate(SpreadSheetBeanConstraints.values());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            validate(SpreadSheetBeanConstraints.values());
        }
    }

    private enum SpreadSheetBeanConstraints implements IConstraint<SpreadSheetBeanPanel> {

        FILE {
                    @Override
                    public String check(SpreadSheetBeanPanel t) {
                        File file = t.getFile();
                        if (file.getPath().isEmpty()) {
                            return "The file path cannot be empty";
                        }
                        if (file.isDirectory()) {
                            return "The file path cannot be a folder";
                        }
                        if (!t.getFileFilter().accept(file)) {
                            return "Invalid file";
                        }
                        return null;
                    }
                },
    }
}
