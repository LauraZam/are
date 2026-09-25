import React, { useState, useEffect } from 'react';

const STUDENT_ID = 1;
const API_BASE = "http://localhost:8080/api";

export default function App() {
  const [skills, setSkills] = useState([]);
  const [newSkill, setNewSkill] = useState("");
  const [recommendations, setRecommendations] = useState([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const skillsRes = await fetch(`${API_BASE}/students/${STUDENT_ID}/skills`);
      const skillsData = await skillsRes.json();
      setSkills(skillsData);

      const recsRes = await fetch(`${API_BASE}/recommendations/student/${STUDENT_ID}`);
      const recsData = await recsRes.json();
      setRecommendations(recsData);
    } catch (err) {
      console.error("Failed to connect to Spring Boot backend:", err);
    }
  };

  const handleAddSkill = async (e) => {
    e.preventDefault();
    if (!newSkill.trim()) return;

    await fetch(`${API_BASE}/students/${STUDENT_ID}/skills`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ skill: newSkill.trim() })
    });

    setNewSkill("");
    loadData();
  };

  const handleRemoveSkill = async (skillToRemove) => {
    await fetch(`${API_BASE}/students/${STUDENT_ID}/skills?skill=${encodeURIComponent(skillToRemove)}`, {
      method: 'DELETE'
    });
    loadData();
  };

  return (
    <div className="min-h-screen bg-gray-50 text-gray-800 p-8">
      <div className="max-w-5xl mx-auto">
        <header className="mb-8 border-b pb-4">
          <h1 className="text-3xl font-bold text-blue-600">Academic Recommendation Engine</h1>
          <p className="text-sm text-gray-500 mt-1">Thesis Project</p>
        </header>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 md:col-span-1">
            <h2 className="text-lg font-semibold mb-4 text-gray-700">Student Profile</h2>
            
            <div className="mb-3">
              <span className="text-xs text-gray-400 uppercase tracking-wider">Student Name</span>
              <p className="font-medium text-gray-900">Jane Smith</p>
            </div>
            
            <div className="mb-4">
              <span className="text-xs text-gray-400 uppercase tracking-wider">Major Track</span>
              <span className="block mt-1 bg-blue-50 text-blue-700 text-xs px-2.5 py-1 rounded-md font-semibold w-fit">
                COMPUTER_SCIENCE
              </span>
            </div>

            <hr className="my-4 border-gray-100" />

            <h3 className="text-sm font-semibold mb-3 text-gray-700 uppercase tracking-wider">Live Competencies</h3>
            <div className="flex flex-wrap gap-2 mb-4">
              {skills.map((skill, index) => (
                <span key={index} className="bg-gray-100 text-gray-700 text-xs px-3 py-1 rounded-full flex items-center gap-1.5 font-medium">
                  {skill}
                  <button 
                    onClick={() => handleRemoveSkill(skill)} 
                    className="text-gray-400 hover:text-red-600 font-bold text-sm">
                    &times;
                  </button>
                </span>
              ))}
            </div>

            <form onSubmit={handleAddSkill} className="space-y-2">
              <input 
                type="text" 
                value={newSkill}
                onChange={(e) => setNewSkill(e.target.value)}
                placeholder="Add skill (e.g., Docker)" 
                className="w-full border border-gray-200 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button 
                type="submit" 
                className="w-full bg-blue-600 text-white py-2 rounded-xl text-sm font-medium hover:bg-blue-700 transition shadow-sm">
                Add Skill & Recalculate
              </button>
            </form>
          </div>

          <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 md:col-span-2">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-lg font-semibold text-gray-700">Smart Course Recommendations</h2>
              <button onClick={loadData} className="text-xs text-blue-600 hover:underline font-medium">Refresh</button>
            </div>

            <div className="space-y-4">
              {recommendations.length === 0 ? (
                <p className="text-sm text-gray-400">No active course recommendations available.</p>
              ) : (
                recommendations.map((rec) => (
                  <div key={rec.id} className="border border-gray-100 bg-gray-50/50 p-4 rounded-xl hover:border-blue-200 transition">
                    <div className="flex justify-between items-start">
                      <div>
                        <span className="text-xs font-semibold text-blue-600 bg-blue-100/60 px-2 py-0.5 rounded">
                          {rec.courseCode}
                        </span>
                        <h3 className="font-bold text-gray-900 mt-1.5">{rec.title}</h3>
                      </div>
                      <div className="text-right">
                        <span className="text-xl font-extrabold text-emerald-600">{rec.matchScore.toFixed(0)}%</span>
                        <p className="text-[10px] text-gray-400 uppercase tracking-wide">Match Score</p>
                      </div>
                    </div>
                    <div className="mt-4 flex justify-between items-center text-xs text-gray-500 border-t border-gray-200/60 pt-3">
                      <span>Credits: <strong className="text-gray-700">{rec.credits}</strong></span>
                      <span>Matching Competencies: <strong className="text-gray-700">{rec.matchingSkillCount}</strong></span>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}