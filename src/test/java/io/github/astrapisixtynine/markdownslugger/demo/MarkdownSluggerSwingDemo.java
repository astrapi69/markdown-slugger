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

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;
import io.github.astrapisixtynine.markdownslugger.core.MarkdownProcessor;
import io.github.astrapisixtynine.markdownslugger.slug.DefaultSlugStrategy;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyExtensions;

/**
 * Swing demo application for the markdown-slugger library
 *
 * Demonstrates the complete transformation pipeline: Markdown heading line to heading text, slug,
 * anchor link, anchor ID, and TOC line
 */
public class MarkdownSluggerSwingDemo extends JFrame
{

	private static final Color BG_COLOR = new Color(245, 240, 232);
	private static final Color CARD_BG = Color.WHITE;
	private static final Color TEXT_PRIMARY = new Color(26, 26, 46);
	private static final Color TEXT_SECONDARY = new Color(107, 107, 123);
	private static final Color ACCENT = new Color(26, 26, 46);
	private static final Color BORDER_COLOR = new Color(230, 226, 218);

	private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);
	private static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
	private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 11);
	private static final Font FONT_INPUT = new Font("Monospaced", Font.PLAIN, 16);
	private static final Font FONT_MONO = new Font("Monospaced", Font.PLAIN, 13);
	private static final Font FONT_STEP_LABEL = new Font("SansSerif", Font.PLAIN, 12);
	private static final Font FONT_STEP_VALUE = new Font("Monospaced", Font.BOLD, 13);
	private static final Font FONT_EXAMPLE = new Font("Monospaced", Font.PLAIN, 11);

	private static final String[] EXAMPLES = { "# Introduction", "## Getting Started",
			"### Installation Guide", "## \u00dcber den Wolken", "## Id\u00e9es cr\u00e9atives",
			"# Welcome to the Jungle!", "## What's New in v2.0?", "## Section 1: Introduction",
			"### Caf\u00e9 au lait", "## Sch\u00f6ne Gr\u00fc\u00dfe", "###### Deep Section" };

	private JTextField inputField;
	private JLabel lblHeading;
	private JLabel lblSlug;
	private JLabel lblLink;
	private JLabel lblAnchorId;
	private JLabel lblTocLine;
	private JLabel lblLevel;
	private JTextArea txtPreview;
	private JToggleButton btnStrict;
	private JToggleButton btnDefault;

	private SlugifyConfig strictConfig;
	private SlugifyConfig defaultConfig;

	public MarkdownSluggerSwingDemo()
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
		setMinimumSize(new Dimension(680, 700));
		setPreferredSize(new Dimension(720, 780));

		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setBackground(BG_COLOR);
		root.setBorder(new EmptyBorder(28, 32, 28, 32));

		root.add(buildHeader());
		root.add(Box.createVerticalStrut(24));
		root.add(buildInputSection());
		root.add(Box.createVerticalStrut(12));
		root.add(buildConfigToggle());
		root.add(Box.createVerticalStrut(20));
		root.add(buildResultCard());
		root.add(Box.createVerticalStrut(16));
		root.add(buildPreviewCard());
		root.add(Box.createVerticalStrut(20));
		root.add(buildExamplesSection());
		root.add(Box.createVerticalGlue());
		root.add(buildFooter());

		JScrollPane scrollPane = new JScrollPane(root);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		setContentPane(scrollPane);
		pack();
		setLocationRelativeTo(null);
	}

	private JPanel buildHeader()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(BG_COLOR);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel title = new JLabel("markdown-slugger");
		title.setFont(FONT_TITLE);
		title.setForeground(TEXT_PRIMARY);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel subtitle = new JLabel("Header to Anchor Link Transformation");
		subtitle.setFont(FONT_SUBTITLE);
		subtitle.setForeground(TEXT_SECONDARY);
		subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(title);
		panel.add(Box.createVerticalStrut(4));
		panel.add(subtitle);
		return panel;
	}

	private JPanel buildInputSection()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(BG_COLOR);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel label = new JLabel("MARKDOWN HEADING");
		label.setFont(FONT_LABEL);
		label.setForeground(TEXT_SECONDARY);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputField = new JTextField("## What's New in v2.0?");
		inputField.setFont(FONT_INPUT);
		inputField.setForeground(TEXT_PRIMARY);
		inputField.setBackground(CARD_BG);
		inputField.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(ACCENT, 2),
			BorderFactory.createEmptyBorder(10, 12, 10, 12)));
		inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
		inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputField.getDocument().addDocumentListener(new DocumentListener()
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
		});

		panel.add(label);
		panel.add(Box.createVerticalStrut(8));
		panel.add(inputField);
		return panel;
	}

	private JPanel buildConfigToggle()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panel.setBackground(BG_COLOR);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel label = new JLabel("CONFIG");
		label.setFont(FONT_LABEL);
		label.setForeground(TEXT_SECONDARY);

		btnStrict = createToggleButton("Strict", true);
		btnDefault = createToggleButton("Default", false);

		ButtonGroup group = new ButtonGroup();
		group.add(btnStrict);
		group.add(btnDefault);

		btnStrict.addActionListener(e -> updateResult());
		btnDefault.addActionListener(e -> updateResult());

		panel.add(label);
		panel.add(Box.createHorizontalStrut(4));
		panel.add(btnStrict);
		panel.add(btnDefault);
		return panel;
	}

	private JToggleButton createToggleButton(String text, boolean selected)
	{
		JToggleButton btn = new JToggleButton(text, selected);
		btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(72, 30));
		btn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		btn.addChangeListener(e -> {
			if (btn.isSelected())
			{
				btn.setBackground(ACCENT);
				btn.setForeground(BG_COLOR);
			}
			else
			{
				btn.setBackground(CARD_BG);
				btn.setForeground(TEXT_SECONDARY);
			}
		});
		if (selected)
		{
			btn.setBackground(ACCENT);
			btn.setForeground(BG_COLOR);
		}
		else
		{
			btn.setBackground(CARD_BG);
			btn.setForeground(TEXT_SECONDARY);
		}
		return btn;
	}

	private JPanel buildResultCard()
	{
		JPanel card = createCard();

		lblLevel = new JLabel("h2");
		lblLevel.setFont(new Font("Monospaced", Font.BOLD, 11));
		lblLevel.setForeground(TEXT_SECONDARY);
		lblLevel.setOpaque(true);
		lblLevel.setBackground(new Color(240, 238, 232));
		lblLevel.setBorder(new EmptyBorder(2, 8, 2, 8));
		lblLevel.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblHeading = new JLabel();
		lblSlug = new JLabel();
		lblLink = new JLabel();
		lblAnchorId = new JLabel();
		lblTocLine = new JLabel();

		card.add(lblLevel);
		card.add(Box.createVerticalStrut(12));
		card.add(buildStepRow(1, "Heading", lblHeading, false));
		card.add(buildSeparator());
		card.add(buildStepRow(2, "Slug", lblSlug, true));
		card.add(buildSeparator());
		card.add(buildStepRow(3, "Link", lblLink, true));
		card.add(buildSeparator());
		card.add(buildStepRow(4, "Anchor ID", lblAnchorId, true));
		card.add(buildSeparator());
		card.add(buildStepRow(5, "TOC Line", lblTocLine, true));

		return card;
	}

	private JPanel buildStepRow(int step, String labelText, JLabel valueLabel, boolean mono)
	{
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		row.setBackground(CARD_BG);
		row.setBorder(new EmptyBorder(8, 0, 8, 0));
		row.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel badge = new JLabel(String.valueOf(step), SwingConstants.CENTER);
		badge.setFont(new Font("SansSerif", Font.BOLD, 11));
		badge.setForeground(BG_COLOR);
		badge.setOpaque(true);
		badge.setBackground(ACCENT);
		badge.setPreferredSize(new Dimension(22, 22));
		badge.setMinimumSize(new Dimension(22, 22));
		badge.setMaximumSize(new Dimension(22, 22));

		JLabel label = new JLabel(labelText);
		label.setFont(FONT_STEP_LABEL);
		label.setForeground(TEXT_SECONDARY);
		label.setPreferredSize(new Dimension(80, 20));
		label.setMinimumSize(new Dimension(80, 20));
		label.setMaximumSize(new Dimension(80, 20));

		valueLabel.setFont(mono ? FONT_STEP_VALUE : new Font("SansSerif", Font.PLAIN, 13));
		valueLabel.setForeground(TEXT_PRIMARY);

		row.add(badge);
		row.add(Box.createHorizontalStrut(10));
		row.add(label);
		row.add(Box.createHorizontalStrut(8));
		row.add(valueLabel);
		row.add(Box.createHorizontalGlue());

		return row;
	}

	private JPanel buildPreviewCard()
	{
		JPanel card = createCard();

		JLabel label = new JLabel("RENDERED PREVIEW");
		label.setFont(FONT_LABEL);
		label.setForeground(TEXT_SECONDARY);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtPreview = new JTextArea();
		txtPreview.setFont(FONT_MONO);
		txtPreview.setForeground(TEXT_PRIMARY);
		txtPreview.setBackground(new Color(248, 246, 240));
		txtPreview.setEditable(false);
		txtPreview.setLineWrap(true);
		txtPreview.setWrapStyleWord(true);
		txtPreview.setBorder(new EmptyBorder(10, 12, 10, 12));

		card.add(label);
		card.add(Box.createVerticalStrut(10));
		card.add(txtPreview);

		return card;
	}

	private JPanel buildExamplesSection()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(BG_COLOR);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel label = new JLabel("EXAMPLES");
		label.setFont(FONT_LABEL);
		label.setForeground(TEXT_SECONDARY);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
		buttonPanel.setBackground(BG_COLOR);
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		for (String example : EXAMPLES)
		{
			JButton btn = new JButton(example);
			btn.setFont(FONT_EXAMPLE);
			btn.setFocusPainted(false);
			btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btn.setBackground(CARD_BG);
			btn.setForeground(TEXT_SECONDARY);
			btn.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_COLOR),
				BorderFactory.createEmptyBorder(3, 8, 3, 8)));
			btn.addActionListener(e -> {
				inputField.setText(example);
				updateResult();
			});
			buttonPanel.add(btn);
		}

		panel.add(label);
		panel.add(Box.createVerticalStrut(8));
		panel.add(buttonPanel);
		return panel;
	}

	private JPanel buildFooter()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(BG_COLOR);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
			new EmptyBorder(12, 0, 0, 0)));

		JLabel footer = new JLabel("io.github.astrapi69 / markdown-slugger");
		footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
		footer.setForeground(new Color(155, 155, 171));
		panel.add(footer);
		return panel;
	}

	private JPanel createCard()
	{
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(CARD_BG);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER_COLOR),
			new EmptyBorder(16, 20, 16, 20)));
		card.setAlignmentX(Component.LEFT_ALIGNMENT);
		return card;
	}

	private JSeparator buildSeparator()
	{
		JSeparator sep = new JSeparator();
		sep.setForeground(new Color(240, 238, 234));
		sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		sep.setAlignmentX(Component.LEFT_ALIGNMENT);
		return sep;
	}

	private SlugifyConfig getActiveConfig()
	{
		return btnStrict.isSelected() ? strictConfig : defaultConfig;
	}

	private void updateResult()
	{
		String input = inputField.getText().trim();
		if (input.isEmpty())
		{
			clearResult();
			return;
		}

		// Use the library pipeline to process
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
			// Fallback: treat entire input as plain text
			String slug = SlugifyExtensions.slugify(input, config);
			lblLevel.setText("plain");
			lblHeading.setText(input);
			lblSlug.setText(slug);
			lblLink.setText("[" + input + "](#" + slug + ")");
			lblAnchorId.setText("-");
			lblTocLine.setText("- [" + input + "](#" + slug + ")");
			txtPreview.setText("- [" + input + "](#" + slug + ")");
			return;
		}

		String heading = headings.get(0);
		String slug = slugs.get(0);
		int level = levels.get(0);

		String link = "[" + heading + "](#" + slug + ")";
		String hashes = "#".repeat(level);
		String anchorId = hashes + " " + heading + " {#" + slug + "}";
		String indent = "  ".repeat(Math.max(0, level - 1));
		String tocLine = indent + "- " + link;

		lblLevel.setText("h" + level);
		lblHeading.setText(heading);
		lblSlug.setText(slug);
		lblLink.setText(link);
		lblAnchorId.setText(anchorId);
		lblTocLine.setText(tocLine);

		txtPreview.setText(tocLine + "\n\n<!-- in document -->\n" + anchorId);
	}

	private void clearResult()
	{
		lblLevel.setText("-");
		lblHeading.setText("");
		lblSlug.setText("");
		lblLink.setText("");
		lblAnchorId.setText("");
		lblTocLine.setText("");
		txtPreview.setText("");
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
			new MarkdownSluggerSwingDemo().setVisible(true);
		});
	}
}
