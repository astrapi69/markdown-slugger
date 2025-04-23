package io.github.astrapisixtynine.markdownslugger.test.object.factory;

import java.util.ArrayList;
import java.util.List;

import io.github.astrapisixtynine.markdownslugger.slug.ReplacementRule;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;

/**
 * Factory class for providing reusable test objects and configurations
 */
public class TestObjectFactory
{

	public static SlugifyConfig getSlugifyConfig(List<ReplacementRule> rules)
	{
		return SlugifyConfig.builder().replacementRules(rules).toLowerCase(true)
			.stripNonAlphanumeric(false).whitespaceReplacement("-").trimEdges(false)
			.removeAccents(false).collapseDashes(false).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();
	}

	public static List<ReplacementRule> getReplacementRules()
	{
		List<ReplacementRule> rules = new ArrayList<>();

		rules.add(ReplacementRule.builder().pattern("\\?").replacement("").regex(true).build());
		rules.add(ReplacementRule.builder().pattern("❖").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern(": ").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern(":").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("’").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("▷").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("\\.").replacement("").regex(true).build());
		rules.add(ReplacementRule.builder().pattern("☰").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("⚠").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✧").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✦").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("↺").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("⚡").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✉").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("⌂").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✎").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("¶").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("»").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("▣").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✓").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern(",").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("★").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("→").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("\\(").replacement("").regex(true).build());
		rules.add(ReplacementRule.builder().pattern("\\)").replacement("").regex(true).build());
		rules.add(ReplacementRule.builder().pattern("✕ ").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✷").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("– ").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("·").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("& ").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("&").replacement("").regex(false).build());

		return rules;
	}

	public static List<ReplacementRule> getShortTestRules()
	{
		List<ReplacementRule> rules = new ArrayList<>();

		rules.add(ReplacementRule.builder().pattern(": ").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern(":").replacement("-").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("’").replacement("").regex(false).build());
		rules.add(ReplacementRule.builder().pattern("✎ ").replacement("-").regex(false).build());

		return rules;
	}
}
