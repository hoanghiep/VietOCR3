/**
 * Copyright @ 2008 Quan Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sourceforge.vietocr;

import java.io.File;
import java.util.*;
import javax.swing.*;

public class GuiWithSettings extends GuiWithLaF {

    private final String strWatchFolder = "WatchFolder";
    private final String strOutputFolder = "OutputFolder";
    private final String strWatchEnabled = "WatchEnabled";
    private final String strWatchDeskewEnable = "WatchDeskewEnable";
    private final String strTessLibEnabled = "TessLibEnabled";
    private final String strBatchOutputFormat = "BatchOutputFormat";
    
    protected String watchFolder;
    protected String outputFolder;
    protected boolean watchEnabled;
    protected boolean watchDeskewEnabled;
    protected String outputFormat;
    private OptionsDialog optionsDialog;

    public GuiWithSettings() {
        watchFolder = prefs.get(strWatchFolder, System.getProperty("user.home"));
        if (!new File(watchFolder).exists()) watchFolder = System.getProperty("user.home");
        outputFolder = prefs.get(strOutputFolder, System.getProperty("user.home"));
        if (!new File(outputFolder).exists()) outputFolder = System.getProperty("user.home");
        watchEnabled = prefs.getBoolean(strWatchEnabled, false);
        watchDeskewEnabled = prefs.getBoolean(strWatchDeskewEnable, false);
        tessLibEnabled = prefs.getBoolean(strTessLibEnabled, false);
        outputFormat = prefs.get(strBatchOutputFormat, "text");
    }

    @Override
    void jMenuItemOptionsActionPerformed(java.awt.event.ActionEvent evt) {
        if (optionsDialog == null) {
            optionsDialog = new OptionsDialog(this, true);
        }

        optionsDialog.setWatchFolder(watchFolder);
        optionsDialog.setOutputFolder(outputFolder);
        optionsDialog.setWatchEnabled(watchEnabled);
        optionsDialog.setWatchDeskewEnabled(watchDeskewEnabled);
        optionsDialog.setDangAmbigsPath(dangAmbigsPath);
        optionsDialog.setDangAmbigsEnabled(dangAmbigsOn);
        optionsDialog.setCurLangCode(curLangCode);
        optionsDialog.setReplaceHyphensEnabled(replaceHyphensEnabled);
        optionsDialog.setRemoveHyphensEnabled(removeHyphensEnabled);
        optionsDialog.setSelectedOutputFormat(outputFormat);

        if (optionsDialog.showDialog() == JOptionPane.OK_OPTION) {
            watchFolder = optionsDialog.getWatchFolder();
            outputFolder = optionsDialog.getOutputFolder();
            watchEnabled = optionsDialog.isWatchEnabled();
            watchDeskewEnabled = optionsDialog.isWatchDeskewEnabled();
            dangAmbigsPath = optionsDialog.getDangAmbigsPath();
            dangAmbigsOn = optionsDialog.isDangAmbigsEnabled();
            replaceHyphensEnabled = optionsDialog.isReplaceHyphensEnabled();
            removeHyphensEnabled = optionsDialog.isRemoveHyphensEnabled();
            outputFormat = optionsDialog.getSelectedOutputFormat();

            updateWatch(watchFolder, watchEnabled);
        }
    }

    protected void updateWatch(String watchFolder, boolean watchEnabled) {
        // override in subclass
    }

    @Override
    void jMenuItemDownloadLangDataActionPerformed(java.awt.event.ActionEvent evt) {
        DownloadDialog dialog = new DownloadDialog(this, true);
        dialog.setLookupISO639(lookupISO639);
        dialog.setLookupISO_3_1_Codes(lookupISO_3_1_Codes);
        dialog.setInstalledLanguages(installedLanguages);
        dialog.setTessdataDir(new File(datapath));
        dialog.setVisible(true);
    }

    @Override
    void quit() {
        prefs.put(strWatchFolder, watchFolder);
        prefs.put(strOutputFolder, outputFolder);
        prefs.putBoolean(strWatchEnabled, watchEnabled);
        prefs.putBoolean(strWatchDeskewEnable, watchDeskewEnabled);
        prefs.putBoolean(strTessLibEnabled, tessLibEnabled);
        prefs.put(strBatchOutputFormat, outputFormat);
        super.quit();
    }

    @Override
    void changeUILanguage(final Locale locale) {
        super.changeUILanguage(locale);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (optionsDialog != null) {
                    optionsDialog.changeUILanguage(locale);
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        selectedUILang = prefs.get(strUILanguage, "en");
        Locale.setDefault(getLocale(selectedUILang));

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiWithSettings().setVisible(true);
            }
        });
    }
}
