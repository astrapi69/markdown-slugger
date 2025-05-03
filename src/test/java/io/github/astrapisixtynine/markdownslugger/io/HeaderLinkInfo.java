package io.github.astrapisixtynine.markdownslugger.io;

import java.util.Collection;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeaderLinkInfo
{
	Collection<String> markdownLines;
	Collection<String> headings;
	Set<String> fragmentIds;
}
