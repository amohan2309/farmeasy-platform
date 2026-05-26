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
  const locale = localStorage.getItem('locale') || 'en';

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

  const t = (en: string, hi: string) => (locale === 'hi' ? hi : en);

  return (
    <div className="page-inner">
      <Link to="/apps" className="back">← {t('Applications', 'ऐप्स')}</Link>
      <h2>{t('Menu', 'मेनू')}</h2>

      <section>
        <h3 className="section-title">{t('Chapters', 'अध्याय')}</h3>
        <ul className="menu-list">
          {chapters.map((c) => (
            <li key={c.id}>
              <button type="button" className={selectedChapter === c.id ? 'active' : ''} onClick={() => selectChapter(c.id)}>
                {locale === 'hi' ? (c.titleHi || c.titleEn) : c.titleEn}
              </button>
            </li>
          ))}
        </ul>
      </section>

      {topics.length > 0 && (
        <section>
          <h3 className="section-title">{t('Topics', 'विषय')}</h3>
          <ul className="menu-list">
            {topics.map((item) => (
              <li key={item.id}>
                <button type="button" className={selectedTopic === item.id ? 'active' : ''} onClick={() => selectTopic(item.id)}>
                  {locale === 'hi' ? (item.titleHi || item.titleEn) : item.titleEn}
                </button>
              </li>
            ))}
          </ul>
        </section>
      )}

      {subtopics.length > 0 && (
        <section>
          <h3 className="section-title">{t('Subtopics', 'उप-विषय')}</h3>
          <ul className="menu-list plain">
            {subtopics.map((s) => (
              <li key={s.id} className="subtopic-item">
                {locale === 'hi' ? (s.titleHi || s.titleEn) : s.titleEn}
                <span>({s.contentType})</span>
              </li>
            ))}
          </ul>
        </section>
      )}
    </div>
  );
}
