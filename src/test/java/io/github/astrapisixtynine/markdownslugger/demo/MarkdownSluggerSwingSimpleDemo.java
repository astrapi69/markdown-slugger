/**
 * The MIT License
 *
 * Copyright (C) 2025 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapisixtynine.markdownslugger.demo;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;
import io.github.astrapisixtynine.markdownslugger.core.MarkdownProcessor;
import io.github.astrapisixtynine.markdownslugger.slug.DefaultSlugStrategy;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyExtensions;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

/**
 * Swing demo application for the markdown-slugger library
 *
 * Demonstrates the complete transformation pipeline: Markdown heading line to heading text, slug,
 * anchor link, anchor ID, and TOC line. All output fields are selectable and copyable.
 */
public class MarkdownSluggerSwingSimpleDemo extends JFrame
{

	private static final String[] EXAMPLES = { "# Introduction", "## Getting Started",
			"### Installation Guide", "## \u00dcber den Wolken", "## Id\u00e9es cr\u00e9atives",
			"# Welcome to the Jungle!", "## What's New in v2.0?", "## Section 1: Introduction",
			"### Caf\u00e9 au lait", "## Sch\u00f6ne Gr\u00fc\u00dfe", "###### Deep Section" };

	private JTextField inputField;
	private JTextField txtHeading;
	private JTextField txtSlug;
	private JTextField txtLink;
	private JTextField txtAnchorId;
	private JTextField txtTocLine;
	private JComboBox<String> configCombo;
	private JComboBox<String> examplesCombo;

	private SlugifyConfig strictConfig;
	private SlugifyConfig defaultConfig;

	public MarkdownSluggerSwingSimpleDemo()
	{
		super("markdown-slugger Demo");
		initConfigs();
		initUI();
		updateResult();
	}

	private void initConfigs()
	{
		strictConfig = SlugifyConfig.builder()
				.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
				.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
				.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
				.build();

		defaultConfig = SlugifyConfig.DEFAULT_CONFIG;
	}

	private void initUI()
	{
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel main = new JPanel(new GridBagLayout());
		main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		int row = 0;

		// Input
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		main.add(new JLabel("Markdown Heading:"), gbc);

		inputField = new JTextField("## What's New in v2.0?", 40);
		inputField.getDocument().addDocumentListener(new SimpleDocumentListener());
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		main.add(inputField, gbc);

		// Config
		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		main.add(new JLabel("Config:"), gbc);

		configCombo = new JComboBox<>(new String[] { "Strict", "Default" });
		configCombo.addActionListener(e -> updateResult());
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		main.add(configCombo, gbc);

		// Examples
		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		main.add(new JLabel("Examples:"), gbc);

		examplesCombo = new JComboBox<>(EXAMPLES);
		examplesCombo.addActionListener(e -> {
			String selected = (String)examplesCombo.getSelectedItem();
			if (selected != null)
			{
				inputField.setText(selected);
			}
		});
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		main.add(examplesCombo, gbc);

		// Separator
		row++;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		main.add(new JSeparator(), gbc);
		gbc.gridwidth = 1;

		// Output fields
		row++;
		txtHeading = addOutputRow(main, gbc, row, "Heading:");
		row++;
		txtSlug = addOutputRow(main, gbc, row, "Slug:");
		row++;
		txtLink = addOutputRow(main, gbc, row, "Link:");
		row++;
		txtAnchorId = addOutputRow(main, gbc, row, "Anchor ID:");
		row++;
		txtTocLine = addOutputRow(main, gbc, row, "TOC Line:");

		setContentPane(main);
		pack();
		setLocationRelativeTo(null);
	}

	private JTextField addOutputRow(JPanel panel, GridBagConstraints gbc, int row, String label)
	{
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		panel.add(new JLabel(label), gbc);

		JTextField field = new JTextField(40);
		field.setEditable(false);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		panel.add(field, gbc);

		return field;
	}

	private SlugifyConfig getActiveConfig()
	{
		return configCombo.getSelectedIndex() == 0 ? strictConfig : defaultConfig;
	}

	private void updateResult()
	{
		String input = inputField.getText().trim();
		if (input.isEmpty())
		{
			txtHeading.setText("");
			txtSlug.setText("");
			txtLink.setText("");
			txtAnchorId.setText("");
			txtTocLine.setText("");
			return;
		}

		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(input);

		SlugifyConfig config = getActiveConfig();
		MarkdownProcessor processor = MarkdownProcessor
				.defaultPipeline(new DefaultSlugStrategy(config));
		processor.process(context);

		List<String> headings = context.getHeadings();
		List<String> slugs = context.getSlugs();
		List<Integer> levels = context.getHeadingLevels();

		if (headings.isEmpty())
		{
			String slug = SlugifyExtensions.slugify(input, config);
			txtHeading.setText(input);
			txtSlug.setText(slug);
			txtLink.setText("[" + input + "](#" + slug + ")");
			txtAnchorId.setText("");
			txtTocLine.setText("- [" + input + "](#" + slug + ")");
			return;
		}

		String heading = headings.get(0);
		String slug = slugs.get(0);
		int level = levels.get(0);

		String link = "[" + heading + "](#" + slug + ")";
		String anchorId = "#".repeat(level) + " " + heading + " {#" + slug + "}";
		String indent = "  ".repeat(Math.max(0, level - 1));
		String tocLine = indent + "- " + link;

		txtHeading.setText(heading);
		txtSlug.setText(slug);
		txtLink.setText(link);
		txtAnchorId.setText(anchorId);
		txtTocLine.setText(tocLine);
	}

	private class SimpleDocumentListener implements DocumentListener
	{
		@Override
		public void insertUpdate(DocumentEvent e)
		{
			updateResult();
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			updateResult();
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			updateResult();
		}
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception ignored)
			{
			}
			new MarkdownSluggerSwingSimpleDemo().setVisible(true);
		});
	}
}
