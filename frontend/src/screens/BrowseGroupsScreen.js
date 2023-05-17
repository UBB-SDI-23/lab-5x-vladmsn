import React, { useState, useEffect } from "react";
import Group from "../components/Group";
import axios from "../axios";
import ListGroup from "react-bootstrap/ListGroup";
import { Button, ButtonGroup, FormControl } from 'react-bootstrap';


const BrowseGroupsScreen = () => {
  const [groups, setGroups] = useState([]);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const size = 5; 

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const { data } = await axios.get(`/api/v1/groups?page=${page}&size=${size}`);

        setTotalPages(data.totalPages);
        setGroups(data.content);
      }
      catch (error) {
        console.log(error);
      }
    };

    fetchGroups();
  }, [page, size]);

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(prevPage => prevPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(prevPage => prevPage - 1);
    }
  };

  const handlePageChange = (event) => {
    const newPage = Number(event.target.value);
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };

  return (
    <>
      <h1>Groups</h1>
      <div className="d-flex justify-content-center">
        <ListGroup className="w-100">
          {groups.map((group) => (
            <ListGroup.Item key={group.id} className="border-0">
              <Group group={group}/>
            </ListGroup.Item>
          ))}
        </ListGroup>
      </div>

      <div className="d-flex justify-content-center">
        <ButtonGroup>
          <Button onClick={handlePreviousPage} disabled={page === 0}>
            Previous
          </Button>
          <FormControl
            type="number"
            value={page+1}
            onChange={handlePageChange}
            style={{ width: '100px' }}
          />
          <Button onClick={handleNextPage} disabled={page === totalPages - 1}>
            Next
          </Button>
        </ButtonGroup>
      </div>
    </>
  );
};

export default BrowseGroupsScreen;
