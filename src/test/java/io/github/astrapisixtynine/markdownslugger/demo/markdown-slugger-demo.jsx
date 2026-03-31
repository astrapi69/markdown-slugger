import { useState, useCallback, useMemo } from "react";

const REPLACEMENT_RULES = [
  ["a", "a"], ["A", "A"], ["a", "a"], ["A", "A"],
  ["a", "a"], ["A", "A"], ["a", "a"], ["A", "A"],
  ["a", "a"], ["A", "A"],
  ["e", "e"], ["E", "E"], ["e", "e"], ["E", "E"],
  ["e", "e"], ["E", "E"], ["e", "e"], ["E", "E"],
  ["i", "i"], ["I", "I"], ["i", "i"], ["I", "I"],
  ["o", "o"], ["O", "O"], ["o", "o"], ["O", "O"],
  ["o", "o"], ["O", "O"],
  ["u", "u"], ["U", "U"], ["u", "u"], ["U", "U"],
  ["u", "u"], ["U", "U"],
  ["c", "c"], ["C", "C"], ["ss", "ss"],
];

const CHAR_REPLACEMENTS = {
  "\u00e0": "a", "\u00c0": "A", "\u00e2": "a", "\u00c2": "A",
  "\u00e4": "a", "\u00c4": "A", "\u00e1": "a", "\u00c1": "A",
  "\u00e3": "a", "\u00c3": "A",
  "\u00e9": "e", "\u00c9": "E", "\u00e8": "e", "\u00c8": "E",
  "\u00ea": "e", "\u00ca": "E", "\u00eb": "e", "\u00cb": "E",
  "\u00ee": "i", "\u00ce": "I", "\u00ef": "i", "\u00cf": "I",
  "\u00f4": "o", "\u00d4": "O", "\u00f6": "o", "\u00d6": "O",
  "\u00f3": "o", "\u00d3": "O",
  "\u00f9": "u", "\u00d9": "U", "\u00fb": "u", "\u00db": "U",
  "\u00fc": "u", "\u00dc": "U",
  "\u00e7": "c", "\u00c7": "C", "\u00df": "ss",
};

const EMOJI_REGEX = /[\u{1F600}-\u{1F64F}\u{1F300}-\u{1F5FF}\u{1F680}-\u{1F6FF}\u{1F1E0}-\u{1F1FF}\u{2600}-\u{26FF}\u{2700}-\u{27BF}\u{FE00}-\u{FE0F}\u{1F900}-\u{1F9FF}\u{1FA00}-\u{1FA6F}\u{1FA70}-\u{1FAFF}\u{200D}\u{20E3}\u{2B50}\u{2B55}\u{231A}\u{231B}\u{23E9}-\u{23F3}\u{23F8}-\u{23FA}\u{25AA}\u{25AB}\u{25B6}\u{25C0}\u{25FB}-\u{25FE}\u{2614}\u{2615}\u{2648}-\u{2653}\u{267F}\u{2693}\u{26A1}\u{26AA}\u{26AB}\u{26BD}\u{26BE}\u{26C4}\u{26C5}\u{26CE}\u{26D4}\u{26EA}\u{26F2}\u{26F3}\u{26F5}\u{26FA}\u{26FD}\u{2702}\u{2705}\u{2708}-\u{270D}\u{270F}\u{2712}\u{2714}\u{2716}\u{271D}\u{2721}\u{2728}\u{2733}\u{2734}\u{2744}\u{2747}\u{274C}\u{274E}\u{2753}-\u{2755}\u{2757}\u{2763}\u{2764}\u{2795}-\u{2797}\u{27A1}\u{27B0}\u{27BF}\u{2934}\u{2935}\u{2B05}-\u{2B07}\u{2B1B}\u{2B1C}\u{3030}\u{303D}\u{3297}\u{3299}\u{FE0F}\u{200D}\u{20E3}\u{E0020}-\u{E007F}]/gu;

function slugify(text, config = "strict") {
  let slug = text;

  // 1. Apply character replacements
  for (const [char, replacement] of Object.entries(CHAR_REPLACEMENTS)) {
    slug = slug.split(char).join(replacement);
  }

  // 2. Remove emojis
  slug = slug.replace(EMOJI_REGEX, "");

  // 3. Remove accents via NFD normalization (strict mode)
  if (config === "strict") {
    slug = slug.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
    slug = slug.replace(/[^\x00-\x7F]/g, "");
  }

  // 4. Remove punctuation (apostrophes, colons, quotes)
  slug = slug.replace(/[''":]/g, "");

  // 5. Lowercase
  slug = slug.toLowerCase();

  // 6. Strip non-alphanumeric (strict mode)
  if (config === "strict") {
    slug = slug.replace(/[^a-z0-9\s-]/g, "");
  }

  // 7. Replace whitespace with dashes
  slug = slug.replace(/\s+/g, "-");

  // 8. Collapse multiple dashes
  slug = slug.replace(/-{2,}/g, "-");

  // 9. Trim leading/trailing dashes
  slug = slug.replace(/^-+/, "").replace(/-+$/, "");

  return slug;
}

function extractHeadingText(line) {
  const match = line.match(/^(#{1,6})\s+(.*)/);
  if (!match) return { level: 0, text: line };
  return { level: match[1].length, text: match[2] };
}

const EXAMPLES = [
  "# Introduction",
  "## Getting Started",
  "### Installation Guide",
  "## \u00dcber den Wolken",
  "## Id\u00e9es cr\u00e9atives",
  "# Welcome to the Jungle!",
  "## What's New in v2.0?",
  "## Section 1: Introduction",
  "### Caf\u00e9 au lait",
  "# \ud83d\ude80 Launch",
  "## Sch\u00f6ne Gr\u00fc\u00dfe",
  "###### Deep Section",
];

function StepBadge({ n }) {
  return (
    <span style={{
      display: "inline-flex", alignItems: "center", justifyContent: "center",
      width: 22, height: 22, borderRadius: "50%", fontSize: 11, fontWeight: 700,
      background: "#1a1a2e", color: "#e8dcc8", marginRight: 8, flexShrink: 0,
    }}>{n}</span>
  );
}

function TransformationStep({ step, label, value, mono }) {
  return (
    <div style={{
      display: "flex", alignItems: "baseline", gap: 0, padding: "10px 0",
      borderBottom: "1px solid rgba(26,26,46,0.08)",
    }}>
      <StepBadge n={step} />
      <span style={{ fontSize: 12, color: "#6b6b7b", minWidth: 100, fontWeight: 500 }}>{label}</span>
      <span style={{
        fontFamily: mono ? "'JetBrains Mono', 'Fira Code', 'SF Mono', monospace" : "inherit",
        fontSize: 14, color: "#1a1a2e", fontWeight: 500, wordBreak: "break-all", flex: 1,
        background: mono ? "rgba(26,26,46,0.04)" : "none",
        padding: mono ? "2px 6px" : 0, borderRadius: 3,
      }}>
        {value || <span style={{ color: "#b0b0b8", fontStyle: "italic" }}>...</span>}
      </span>
    </div>
  );
}

export default function MarkdownSluggerDemo() {
  const [input, setInput] = useState("## What's New in v2.0?");
  const [config, setConfig] = useState("strict");

  const result = useMemo(() => {
    const trimmed = input.trim();
    if (!trimmed) return null;

    const { level, text } = extractHeadingText(trimmed);
    const slug = slugify(text, config);
    const link = `[${text}](#${slug})`;
    const anchor = level > 0 ? `${"#".repeat(level)} ${text} {#${slug}}` : null;
    const indent = "  ".repeat(Math.max(0, level - 1));
    const tocLine = `${indent}- ${link}`;

    return { level, text, slug, link, anchor, tocLine };
  }, [input, config]);

  const handleExample = useCallback((ex) => setInput(ex), []);

  return (
    <div style={{
      minHeight: "100vh", background: "#f5f0e8",
      fontFamily: "'Literata', 'Georgia', 'Times New Roman', serif",
      padding: "32px 20px",
    }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Literata:ital,opsz,wght@0,7..72,400;0,7..72,600;0,7..72,700;1,7..72,400&family=JetBrains+Mono:wght@400;500;600&display=swap');
        * { box-sizing: border-box; margin: 0; padding: 0; }
        ::selection { background: #1a1a2e; color: #e8dcc8; }
        input:focus { outline: none; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(6px); } to { opacity: 1; transform: translateY(0); } }
        .example-btn {
          background: none; border: 1px solid rgba(26,26,46,0.15); padding: 4px 10px;
          border-radius: 4px; font-size: 12px; cursor: pointer; transition: all 0.15s;
          font-family: 'JetBrains Mono', monospace; color: #4a4a5a; white-space: nowrap;
        }
        .example-btn:hover { background: #1a1a2e; color: #e8dcc8; border-color: #1a1a2e; }
        .config-btn {
          background: none; border: 1px solid rgba(26,26,46,0.2); padding: 6px 14px;
          border-radius: 4px; font-size: 12px; cursor: pointer; transition: all 0.15s;
          font-family: 'Literata', serif; color: #6b6b7b; font-weight: 500;
        }
        .config-btn.active { background: #1a1a2e; color: #e8dcc8; border-color: #1a1a2e; }
        .config-btn:hover:not(.active) { border-color: #1a1a2e; color: #1a1a2e; }
      `}</style>

      <div style={{ maxWidth: 640, margin: "0 auto" }}>
        {/* Header */}
        <div style={{ marginBottom: 40, animation: "fadeIn 0.4s ease" }}>
          <h1 style={{
            fontSize: 28, fontWeight: 700, color: "#1a1a2e", letterSpacing: -0.5,
            lineHeight: 1.2, marginBottom: 6,
          }}>
            markdown-slugger
          </h1>
          <p style={{ fontSize: 14, color: "#6b6b7b", lineHeight: 1.5 }}>
            Header to Anchor Link Transformation
          </p>
        </div>

        {/* Input */}
        <div style={{ marginBottom: 24, animation: "fadeIn 0.5s ease" }}>
          <label style={{
            display: "block", fontSize: 11, fontWeight: 600, color: "#6b6b7b",
            textTransform: "uppercase", letterSpacing: 1, marginBottom: 8,
          }}>
            Markdown Heading
          </label>
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="## My Heading"
            style={{
              width: "100%", padding: "14px 16px", fontSize: 18, border: "2px solid #1a1a2e",
              borderRadius: 6, background: "#fff",
              fontFamily: "'JetBrains Mono', monospace", fontWeight: 500, color: "#1a1a2e",
            }}
          />
        </div>

        {/* Config toggle */}
        <div style={{ display: "flex", gap: 8, marginBottom: 24, animation: "fadeIn 0.55s ease" }}>
          <span style={{ fontSize: 11, fontWeight: 600, color: "#6b6b7b", textTransform: "uppercase", letterSpacing: 1, alignSelf: "center", marginRight: 4 }}>Config</span>
          <button className={`config-btn ${config === "strict" ? "active" : ""}`} onClick={() => setConfig("strict")}>Strict</button>
          <button className={`config-btn ${config === "default" ? "active" : ""}`} onClick={() => setConfig("default")}>Default</button>
        </div>

        {/* Result */}
        {result && (
          <div style={{
            background: "#fff", borderRadius: 8, padding: "20px 24px", marginBottom: 28,
            border: "1px solid rgba(26,26,46,0.1)",
            boxShadow: "0 2px 12px rgba(26,26,46,0.06)",
            animation: "fadeIn 0.5s ease",
          }}>
            {result.level > 0 && (
              <div style={{
                display: "inline-block", fontSize: 11, fontWeight: 600, color: "#6b6b7b",
                background: "rgba(26,26,46,0.06)", padding: "2px 8px", borderRadius: 3,
                marginBottom: 14, fontFamily: "'JetBrains Mono', monospace",
              }}>
                h{result.level}
              </div>
            )}

            <TransformationStep step={1} label="Heading" value={result.text} />
            <TransformationStep step={2} label="Slug" value={result.slug} mono />
            <TransformationStep step={3} label="Link" value={result.link} mono />

            {result.anchor && (
              <TransformationStep step={4} label="Anchor ID" value={result.anchor} mono />
            )}

            <div style={{ borderBottom: "none", paddingTop: 12 }}>
              <TransformationStep step={5} label="TOC Line" value={result.tocLine} mono />
            </div>
          </div>
        )}

        {/* Rendered preview */}
        {result && (
          <div style={{
            background: "#fff", borderRadius: 8, padding: "16px 24px", marginBottom: 28,
            border: "1px solid rgba(26,26,46,0.1)", animation: "fadeIn 0.6s ease",
          }}>
            <div style={{
              fontSize: 11, fontWeight: 600, color: "#6b6b7b", textTransform: "uppercase",
              letterSpacing: 1, marginBottom: 10,
            }}>
              Rendered Preview
            </div>
            <div style={{
              fontFamily: "'JetBrains Mono', monospace", fontSize: 13, color: "#1a1a2e",
              background: "#f8f6f0", padding: 12, borderRadius: 4, lineHeight: 1.6,
              whiteSpace: "pre-wrap",
            }}>
              <span style={{ color: "#8b6914" }}>{result.tocLine}</span>
              {result.anchor && (
                <>
                  {"\n\n"}
                  <span style={{ color: "#6b6b7b" }}>{"<!-- in document -->"}</span>
                  {"\n"}
                  <span style={{ color: "#2d5f2d" }}>{result.anchor}</span>
                </>
              )}
            </div>
          </div>
        )}

        {/* Examples */}
        <div style={{ animation: "fadeIn 0.7s ease" }}>
          <div style={{
            fontSize: 11, fontWeight: 600, color: "#6b6b7b", textTransform: "uppercase",
            letterSpacing: 1, marginBottom: 10,
          }}>
            Examples
          </div>
          <div style={{ display: "flex", flexWrap: "wrap", gap: 6 }}>
            {EXAMPLES.map((ex, i) => (
              <button key={i} className="example-btn" onClick={() => handleExample(ex)}>
                {ex}
              </button>
            ))}
          </div>
        </div>

        {/* Footer */}
        <div style={{
          marginTop: 40, paddingTop: 16, borderTop: "1px solid rgba(26,26,46,0.08)",
          fontSize: 12, color: "#9b9bab", animation: "fadeIn 0.8s ease",
        }}>
          io.github.astrapi69 / markdown-slugger
        </div>
      </div>
    </div>
  );
}
