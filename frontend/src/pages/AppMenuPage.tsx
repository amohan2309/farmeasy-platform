import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { listChapters, listSubtopics, listTopics } from '../api/client';

type Chapter = { id: string; titleEn: string; titleHi: string };
type Topic = { id: string; titleEn: string; titleHi: string };
type Subtopic = { id: string; titleEn: string; titleHi: string; contentType: string };

export default function AppMenuPage() {
  const { appId } = useParams();
  const [chapters, setChapters] = useState<Chapter[]>([]);
  const [topics, setTopics] = useState<Topic[]>([]);
  const [subtopics, setSubtopics] = useState<Subtopic[]>([]);
  const [selectedChapter, setSelectedChapter] = useState<string | null>(null);
  const [selectedTopic, setSelectedTopic] = useState<string | null>(null);

  useEffect(() => {
    if (!appId) return;
    listChapters(appId).then(setChapters);
  }, [appId]);

  async function selectChapter(chapterId: string) {
    setSelectedChapter(chapterId);
    setSelectedTopic(null);
    setSubtopics([]);
    setTopics(await listTopics(chapterId));
  }

  async function selectTopic(topicId: string) {
    setSelectedTopic(topicId);
    setSubtopics(await listSubtopics(topicId));
  }

  return (
    <div className="page">
      <header className="topbar">
        <Link to="/apps" className="back">← Apps</Link>
        <h1>Menu</h1>
      </header>

      <section>
        <h2 className="section-title">Chapters</h2>
        <ul className="menu-list">
          {chapters.map((c) => (
            <li key={c.id}>
              <button type="button" className={selectedChapter === c.id ? 'active' : ''} onClick={() => selectChapter(c.id)}>
                {c.titleHi || c.titleEn}
              </button>
            </li>
          ))}
        </ul>
      </section>

      {topics.length > 0 && (
        <section>
          <h2 className="section-title">Topics</h2>
          <ul className="menu-list">
            {topics.map((t) => (
              <li key={t.id}>
                <button type="button" className={selectedTopic === t.id ? 'active' : ''} onClick={() => selectTopic(t.id)}>
                  {t.titleHi || t.titleEn}
                </button>
              </li>
            ))}
          </ul>
        </section>
      )}

      {subtopics.length > 0 && (
        <section>
          <h2 className="section-title">Subtopics</h2>
          <ul className="menu-list">
            {subtopics.map((s) => (
              <li key={s.id} className="subtopic-item">{s.titleHi || s.titleEn} <span>({s.contentType})</span></li>
            ))}
          </ul>
        </section>
      )}
    </div>
  );
}
