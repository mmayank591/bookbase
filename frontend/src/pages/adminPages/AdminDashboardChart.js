import React from 'react';
import { Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement, 
  Tooltip,
  Legend,
} from 'chart.js';


ChartJS.register(
  ArcElement,
  Tooltip,
  Legend
);

const AdminDashboardChart = ({ totalUsers, totalBooks, overdueBooks }) => {
  const data = {
    labels: ['Total Users', 'Total Books', 'Overdue Books'],
    datasets: [
      {
        label: '# of Items',
        data: [totalUsers, totalBooks, overdueBooks],
        backgroundColor: [
          'rgba(52, 152, 219, 0.6)',
          'rgba(46, 204, 113, 0.6)',
          'transparent',
        ],
        borderColor: [
          'rgba(52, 152, 219, 1)',
          'rgba(46, 204, 113, 1)',
          'rgba(243, 156, 18, 1)',
        ],
        borderWidth: 2,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom', 
      },
      title: {
        display: false,
        text: 'Library Statistics Overview',
      },
    },
  };

  return (
    <div className="adminchart-card">
      <Doughnut data={data} options={options} />
    </div>
  );
};

export default AdminDashboardChart;