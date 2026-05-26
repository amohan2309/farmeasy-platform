#!/usr/bin/env bash
# Run after: gh auth login
set -euo pipefail
cd "$(dirname "$0")/.."

REPO_NAME="${1:-farmeasy-platform}"
VISIBILITY="${2:-public}"

if ! command -v gh >/dev/null; then
  echo "Install GitHub CLI: brew install gh"
  exit 1
fi

gh auth status

if git remote get-url origin >/dev/null 2>&1; then
  echo "Remote origin already set. Pushing..."
  git push -u origin main
else
  gh repo create "$REPO_NAME" --"$VISIBILITY" --source=. --remote=origin --push
fi

echo "Done. Repository: $(gh repo view --json url -q .url)"
