function StatCard({ label, value, detail, tone = '' }) {
  return (
    <article className={`stat-card card ${tone}`.trim()}>
      <span>{label}</span>
      <strong>{value}</strong>
      <small>{detail}</small>
    </article>
  );
}

export default StatCard;
